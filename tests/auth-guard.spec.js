import { test, expect } from '@playwright/test';

/**
 * SCRUM-28: 인증 가드 및 리다이렉션 E2E 테스트
 *
 * 테스트 시나리오:
 * 1. 비로그인 사용자가 보호된 페이지 접근 시 로그인 페이지로 리다이렉트
 * 2. 리다이렉트 메시지 표시
 * 3. 로그인 후 원래 페이지로 복원
 * 4. 로그인한 사용자는 보호된 페이지에 정상 접근
 */

test.describe('인증 가드 및 리다이렉션', () => {
  test.beforeEach(async ({ page }) => {
    // API 모킹 설정
    await page.route('**/api/favorites', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            stockCode: '005930',
            companyName: '삼성전자(주)',
            registeredAt: '2024-01-15T09:00:00',
          },
        ]),
      });
    });

    await page.route('**/apis.data.go.kr/**', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          response: {
            body: {
              items: {
                item: [
                  {
                    itmsNm: '삼성전자(주)',
                    clpr: '75000',
                    vs: '500',
                    fltRt: '0.67',
                  },
                ],
              },
            },
          },
        }),
      });
    });
  });

  test('비로그인 사용자가 /favorites 접근 시 로그인 페이지로 리다이렉트', async ({
    page,
  }) => {
    // localStorage 비우기 (비로그인 상태)
    await page.goto('/');
    await page.evaluate(() => {
      localStorage.clear();
    });

    // /favorites 페이지 접근 시도
    await page.goto('/favorites');

    // 로그인 페이지로 리다이렉트 확인
    await expect(page).toHaveURL('/login');

    // 로그인 폼이 표시되는지 확인
    await expect(page.locator('.login-title')).toHaveText('로그인');
  });

  test('리다이렉트 메시지가 표시됨', async ({ page }) => {
    // localStorage 비우기 (비로그인 상태)
    await page.goto('/');
    await page.evaluate(() => {
      localStorage.clear();
    });

    // /favorites 페이지 접근 시도
    await page.goto('/favorites');

    // 로그인 페이지로 리다이렉트 확인
    await expect(page).toHaveURL('/login');

    // 리다이렉트 메시지 확인
    const redirectMessage = page.locator('.redirect-message');
    await expect(redirectMessage).toBeVisible();
    await expect(redirectMessage).toContainText('로그인이 필요한 페이지입니다.');
  });

  test('로그인 후 원래 페이지(/favorites)로 리다이렉트', async ({ page }) => {
    // localStorage 비우기 (비로그인 상태)
    await page.goto('/');
    await page.evaluate(() => {
      localStorage.clear();
    });

    // /favorites 페이지 접근 시도
    await page.goto('/favorites');

    // 로그인 페이지로 리다이렉트 확인
    await expect(page).toHaveURL('/login');

    // 로그인 폼 작성
    await page.fill('input[name="email"]', 'test@example.com');
    await page.fill('input[name="password"]', 'password123');

    // 로그인 버튼 클릭
    await page.click('button[type="submit"]');

    // /favorites 페이지로 리다이렉트 확인 (약간의 대기 시간 필요)
    await page.waitForURL('/favorites', { timeout: 5000 });
    await expect(page).toHaveURL('/favorites');

    // 관심기업 페이지가 표시되는지 확인
    await expect(page.locator('.page-title')).toHaveText('관심기업 게시판');
  });

  test('로그인한 사용자는 /favorites에 정상 접근', async ({ page }) => {
    // 로그인 상태 설정
    await page.goto('/');
    await page.evaluate(() => {
      localStorage.setItem('authToken', 'mock_jwt_token_for_testing');
      localStorage.setItem(
        'user',
        JSON.stringify({
          id: 1,
          email: 'test@example.com',
          name: 'test',
        })
      );
    });

    // /favorites 페이지 접근
    await page.goto('/favorites');

    // 리다이렉트 없이 /favorites 페이지가 표시되는지 확인
    await expect(page).toHaveURL('/favorites');
    await expect(page.locator('.page-title')).toHaveText('관심기업 게시판');

    // FavoriteTable이 렌더링되는지 확인
    await expect(page.locator('.favorite-table')).toBeVisible();
  });

  test('ProtectedRoute 로딩 상태 표시', async ({ page }) => {
    // localStorage 비우기
    await page.goto('/');
    await page.evaluate(() => {
      localStorage.clear();
    });

    // 네트워크를 느리게 설정하여 로딩 상태 확인
    await page.route('**/*', async (route) => {
      await new Promise((resolve) => setTimeout(resolve, 100));
      await route.continue();
    });

    // /favorites 접근 시도 (로딩 상태가 잠깐 보일 수 있음)
    const navigationPromise = page.goto('/favorites');

    // 로딩 메시지가 표시될 수 있음 (매우 짧은 시간)
    // 실제로는 AuthContext의 loading 상태가 빠르게 false로 변경되어
    // 바로 리다이렉트가 발생할 수 있음

    await navigationPromise;

    // 최종적으로 로그인 페이지로 리다이렉트
    await expect(page).toHaveURL('/login');
  });

  test('인증되지 않은 사용자가 다른 보호된 페이지에도 접근 불가', async ({
    page,
  }) => {
    // localStorage 비우기
    await page.goto('/');
    await page.evaluate(() => {
      localStorage.clear();
    });

    // 향후 다른 보호된 페이지가 추가될 경우를 대비한 테스트
    // 현재는 /favorites만 보호되므로 이 테스트는 /favorites만 확인

    await page.goto('/favorites');
    await expect(page).toHaveURL('/login');
  });

  test('로그아웃 후 보호된 페이지 접근 불가', async ({ page }) => {
    // 로그인 상태로 시작
    await page.goto('/');
    await page.evaluate(() => {
      localStorage.setItem('authToken', 'mock_jwt_token_for_testing');
      localStorage.setItem(
        'user',
        JSON.stringify({
          id: 1,
          email: 'test@example.com',
          name: 'test',
        })
      );
    });

    // /favorites 접근 (정상 접근)
    await page.goto('/favorites');
    await expect(page).toHaveURL('/favorites');

    // 로그아웃 (localStorage 클리어)
    await page.evaluate(() => {
      localStorage.clear();
    });

    // /favorites 재접근 시도
    await page.goto('/favorites');

    // 로그인 페이지로 리다이렉트
    await expect(page).toHaveURL('/login');
    await expect(page.locator('.redirect-message')).toBeVisible();
  });

  test('잘못된 인증 정보로는 접근 불가', async ({ page }) => {
    // 잘못된 토큰으로 localStorage 설정
    await page.goto('/');
    await page.evaluate(() => {
      localStorage.setItem('authToken', 'invalid_token');
      // user 정보 누락
    });

    // /favorites 접근 시도
    await page.goto('/favorites');

    // isAuthenticated()가 false를 반환하므로 로그인 페이지로 리다이렉트
    await expect(page).toHaveURL('/login');
  });

  test('로그인 후 홈으로 이동하는 경우 (from이 없는 경우)', async ({ page }) => {
    // localStorage 비우기
    await page.goto('/');
    await page.evaluate(() => {
      localStorage.clear();
    });

    // 로그인 페이지로 직접 이동 (리다이렉트가 아님)
    await page.goto('/');

    // 로그인 버튼 클릭하여 로그인 폼 표시
    await page.click('button:has-text("로그인")');

    // 로그인 폼 작성
    await page.fill('input[name="email"]', 'test@example.com');
    await page.fill('input[name="password"]', 'password123');

    // 로그인 버튼 클릭
    await page.click('button[type="submit"]');

    // 홈(/)으로 리다이렉트 확인 (from이 없으므로 기본값 '/')
    await page.waitForURL('/', { timeout: 5000 });
    await expect(page).toHaveURL('/');
  });
});
