import { test, expect } from '@playwright/test';

/**
 * 환율 정보 기능 E2E 테스트
 * SCRUM-7: 경제지표 게시판 구현 (환율 정보)
 */

test.describe('Exchange Rate Feature', () => {
  test.beforeEach(async ({ page }) => {
    // 테스트 전에 로컬 환경으로 이동
    await page.goto('http://localhost:5173');
  });

  test('환율 정보 목록이 표시되어야 함', async ({ page }) => {
    // Given: 경제지표 페이지에 접근
    // (현재 홈페이지에 ExchangeRateTable이 있다고 가정)

    // When: 페이지가 로드됨
    await page.waitForLoadState('networkidle');

    // Then: 환율 테이블이 표시되어야 함
    await expect(page.locator('.exchange-rate-container')).toBeVisible();
    await expect(page.locator('.table-title')).toHaveText('환율 정보');

    // And: 테이블 헤더가 있어야 함
    await expect(page.locator('th:has-text("통화코드")')).toBeVisible();
    await expect(page.locator('th:has-text("매매기준율")')).toBeVisible();
    await expect(page.locator('th:has-text("전일대비")')).toBeVisible();
  });

  test('백엔드 API에서 환율 데이터를 가져와야 함', async ({ page }) => {
    // API 응답을 감지하기 위한 Promise 설정
    const responsePromise = page.waitForResponse(
      response => response.url().includes('/api/exchange-rates') && response.status() === 200
    );

    // 페이지 방문
    await page.goto('http://localhost:5173');

    // API 응답 대기
    const response = await responsePromise;

    // 응답 데이터 확인
    const data = await response.json();

    // Then: 주요 10개국 통화 데이터가 있어야 함
    expect(data).toBeInstanceOf(Array);
    expect(data.length).toBeGreaterThan(0);

    // 필수 필드 확인
    const firstItem = data[0];
    expect(firstItem).toHaveProperty('curUnit');
    expect(firstItem).toHaveProperty('curNm');
    expect(firstItem).toHaveProperty('dealBasR');
    expect(firstItem).toHaveProperty('changeAmount');
    expect(firstItem).toHaveProperty('changeRate');
    expect(firstItem).toHaveProperty('changeDirection');
  });

  test('전일대비 변동이 올바르게 표시되어야 함', async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');

    // 환율 테이블 행이 로드될 때까지 대기
    await page.waitForSelector('tbody tr', { timeout: 10000 });

    // 첫 번째 행의 전일대비 컬럼 확인 (6번째 컬럼, 인덱스 5)
    const changeCell = page.locator('tbody tr').first().locator('td').nth(5);
    await expect(changeCell).toBeVisible();

    // 변동 아이콘(▲, ▼, -) 중 하나가 있어야 함
    const changeText = await changeCell.textContent();
    expect(changeText).toMatch(/[▲▼-]/);
  });

  test('새로고침 버튼이 작동해야 함', async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');

    // 새로고침 버튼 찾기
    const refreshButton = page.locator('.btn-refresh');
    await expect(refreshButton).toBeVisible();

    // API 재호출을 감지
    const responsePromise = page.waitForResponse(
      response => response.url().includes('/api/exchange-rates')
    );

    // 새로고침 버튼 클릭
    await refreshButton.click();

    // API가 다시 호출되었는지 확인
    const response = await responsePromise;
    expect(response.status()).toBe(200);
  });

  test('로딩 상태가 표시되어야 함', async ({ page }) => {
    // 네트워크를 느리게 설정
    await page.route('**/api/exchange-rates*', async route => {
      await new Promise(resolve => setTimeout(resolve, 1000));
      await route.continue();
    });

    await page.goto('http://localhost:5173');

    // 로딩 메시지 확인
    const loadingMessage = page.locator('.loading-message');
    await expect(loadingMessage).toBeVisible();
    await expect(loadingMessage).toHaveText(/환율 정보를 불러오는 중/);

    // 로딩이 완료되면 테이블이 표시되어야 함
    await expect(page.locator('.exchange-rate-container')).toBeVisible({ timeout: 15000 });
  });

  test('에러 상태가 올바르게 처리되어야 함', async ({ page }) => {
    // API 요청을 실패하도록 설정
    await page.route('**/api/exchange-rates*', route => {
      route.abort();
    });

    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');

    // 에러 메시지 확인
    const errorMessage = page.locator('.error-message');
    await expect(errorMessage).toBeVisible({ timeout: 10000 });
    await expect(errorMessage).toHaveText(/환율 데이터를 불러오는데 실패했습니다/);

    // "다시 시도" 버튼이 있어야 함
    await expect(page.locator('button:has-text("다시 시도")')).toBeVisible();
  });
});
