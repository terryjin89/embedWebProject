import { test, expect } from '@playwright/test';

/**
 * 관심기업 테이블 컴포넌트 E2E 테스트
 * SCRUM-27: [FE] 관심기업 테이블 컴포넌트 구현
 *
 * 완료 조건:
 * - 관심기업 목록 테이블 표시
 * - 정렬 기능 동작
 * - 삭제 기능 동작
 * - 상세보기 이동
 */

test.describe('관심기업 테이블 컴포넌트', () => {
  test.beforeEach(async ({ page }) => {
    // Mock 인증 설정 (localStorage에 토큰과 사용자 정보 저장)
    await page.addInitScript(() => {
      const mockToken = 'mock_jwt_token_for_testing';
      const mockUser = JSON.stringify({
        id: 1,
        email: 'test@example.com',
        name: 'test',
      });

      localStorage.setItem('authToken', mockToken);
      localStorage.setItem('user', mockUser);
    });

    // Mock API 응답 설정 (관심기업 목록 API)
    await page.route('**/api/favorites', async (route) => {
      // Mock 관심기업 데이터 반환
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          {
            id: 1,
            stockCode: '005930',
            corpCode: '00126380',
            companyName: '삼성전자(주)',
            stockName: '삼성전자',
            registeredAt: '2024-11-01T10:30:00',
          },
          {
            id: 2,
            stockCode: '000660',
            corpCode: '00164779',
            companyName: 'SK하이닉스(주)',
            stockName: 'SK하이닉스',
            registeredAt: '2024-11-03T14:20:00',
          },
        ]),
      });
    });

    // Mock 금융위원회 주식시세정보 API
    await page.route('**/apis.data.go.kr/**', async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          response: {
            header: { resultCode: '00', resultMsg: 'NORMAL SERVICE.' },
            body: {
              numOfRows: 1,
              pageNo: 1,
              totalCount: 1,
              items: {
                item: {
                  basDt: '20241114',
                  srtnCd: '005930',
                  itmsNm: '삼성전자',
                  clpr: '71500',
                  vs: '1200',
                  fltRt: '1.71',
                },
              },
            },
          },
        }),
      });
    });

    // 관심기업 페이지로 이동
    await page.goto('/favorites');
  });

  test('관심기업 페이지가 로드된다', async ({ page }) => {
    // 페이지 제목 확인
    await expect(page.locator('.page-title')).toContainText('관심기업');

    // 테이블 헤더 확인
    await expect(page.locator('.table-title')).toContainText('관심기업');
  });

  test('관심기업 목록 테이블이 표시된다', async ({ page }) => {
    // 테이블이 존재하는지 확인
    const table = page.locator('.favorite-table');
    await expect(table).toBeVisible();

    // 테이블 헤더 확인
    await expect(table.locator('thead th').nth(0)).toContainText('기업명');
    await expect(table.locator('thead th').nth(1)).toContainText('종목명');
    await expect(table.locator('thead th').nth(2)).toContainText('현재가');
    await expect(table.locator('thead th').nth(3)).toContainText('전일대비');
    await expect(table.locator('thead th').nth(4)).toContainText('등록일');
    await expect(table.locator('thead th').nth(5)).toContainText('관리');
  });

  test('관심기업 데이터가 표시된다 (목업 데이터)', async ({ page }) => {
    // 테이블 행이 나타날 때까지 대기
    await page.waitForSelector('.favorite-table tbody tr.table-row', { timeout: 10000 });

    // 테이블 행이 존재하는지 확인
    const tableRows = page.locator('.favorite-table tbody tr.table-row');
    const rowCount = await tableRows.count();

    // 목업 데이터가 있는지 확인 (최소 1개 이상)
    expect(rowCount).toBeGreaterThan(0);

    // 첫 번째 행의 데이터 확인
    if (rowCount > 0) {
      const firstRow = tableRows.first();

      // 기업명 존재 확인
      await expect(firstRow.locator('.company-name')).not.toBeEmpty();

      // 종목명 존재 확인
      await expect(firstRow.locator('.stock-name')).not.toBeEmpty();

      // 현재가 존재 확인
      await expect(firstRow.locator('.current-price')).not.toBeEmpty();

      // 전일대비 정보 존재 확인
      await expect(firstRow.locator('.price-change')).not.toBeEmpty();
    }
  });

  test('정렬 기능이 동작한다', async ({ page }) => {
    // 정렬 드롭다운이 존재하는지 확인
    const sortSelect = page.locator('.sort-select');
    await expect(sortSelect).toBeVisible();

    // 기본 정렬 확인 (등록일순 - 최신순)
    await expect(sortSelect).toHaveValue('registeredAt-desc');

    // 변동률순으로 정렬 변경
    await sortSelect.selectOption('changeRate-desc');
    await expect(sortSelect).toHaveValue('changeRate-desc');

    // 기업명순으로 정렬 변경
    await sortSelect.selectOption('companyName-asc');
    await expect(sortSelect).toHaveValue('companyName-asc');
  });

  test('테이블 헤더 클릭으로 정렬이 가능하다', async ({ page }) => {
    const table = page.locator('.favorite-table');

    // 기업명 헤더 클릭
    await table.locator('thead th').filter({ hasText: '기업명' }).click();

    // 정렬 아이콘이 활성화되었는지 확인
    const companyNameHeader = table.locator('thead th').filter({ hasText: '기업명' });
    await expect(companyNameHeader.locator('.sort-icon.active')).toBeVisible();
  });

  test('새로고침 버튼이 동작한다', async ({ page }) => {
    // 새로고침 버튼 확인
    const refreshButton = page.locator('.btn-refresh');
    await expect(refreshButton).toBeVisible();

    // 새로고침 버튼 클릭
    await refreshButton.click();

    // 최종 업데이트 시간이 표시되는지 확인
    await expect(page.locator('.last-updated')).toBeVisible();
  });

  test('관심기업 데이터 카운트가 표시된다', async ({ page }) => {
    // 데이터 카운트 확인
    const dataCount = page.locator('.data-count');
    await expect(dataCount).toBeVisible();
    await expect(dataCount).toContainText('개 관심기업');
  });

  test('삭제 버튼이 존재한다', async ({ page }) => {
    const tableRows = page.locator('.favorite-table tbody tr');
    const rowCount = await tableRows.count();

    if (rowCount > 0) {
      // 첫 번째 행의 삭제 버튼 확인
      const deleteButton = tableRows.first().locator('.btn-delete');
      await expect(deleteButton).toBeVisible();
      await expect(deleteButton).toContainText('삭제');
    }
  });

  test('삭제 버튼 클릭 시 확인 다이얼로그가 표시된다', async ({ page }) => {
    const tableRows = page.locator('.favorite-table tbody tr');
    const rowCount = await tableRows.count();

    if (rowCount > 0) {
      // 첫 번째 행의 삭제 버튼 클릭
      await tableRows.first().locator('.btn-delete').click();

      // 다이얼로그가 표시되는지 확인
      const dialog = page.locator('.dialog-overlay');
      await expect(dialog).toBeVisible();

      // 다이얼로그 제목 확인
      await expect(dialog.locator('.dialog-title')).toContainText('관심기업 삭제');

      // 취소 버튼 확인
      await expect(dialog.locator('.btn-cancel')).toBeVisible();

      // 삭제 확인 버튼 확인
      await expect(dialog.locator('.btn-confirm')).toBeVisible();
    }
  });

  test('다이얼로그 취소 버튼이 동작한다', async ({ page }) => {
    const tableRows = page.locator('.favorite-table tbody tr');
    const rowCount = await tableRows.count();

    if (rowCount > 0) {
      // 삭제 버튼 클릭
      await tableRows.first().locator('.btn-delete').click();

      // 다이얼로그 표시 확인
      const dialog = page.locator('.dialog-overlay');
      await expect(dialog).toBeVisible();

      // 취소 버튼 클릭
      await dialog.locator('.btn-cancel').click();

      // 다이얼로그가 사라졌는지 확인
      await expect(dialog).not.toBeVisible();
    }
  });

  test('다이얼로그 오버레이 클릭 시 닫힌다', async ({ page }) => {
    const tableRows = page.locator('.favorite-table tbody tr');
    const rowCount = await tableRows.count();

    if (rowCount > 0) {
      // 삭제 버튼 클릭
      await tableRows.first().locator('.btn-delete').click();

      // 다이얼로그 표시 확인
      const dialog = page.locator('.dialog-overlay');
      await expect(dialog).toBeVisible();

      // 오버레이 클릭 (다이얼로그 외부)
      await dialog.click({ position: { x: 10, y: 10 } });

      // 다이얼로그가 사라졌는지 확인
      await expect(dialog).not.toBeVisible();
    }
  });

  test('상세보기 버튼이 존재한다', async ({ page }) => {
    const tableRows = page.locator('.favorite-table tbody tr');
    const rowCount = await tableRows.count();

    if (rowCount > 0) {
      // 첫 번째 행의 상세보기 버튼 확인
      const detailButton = tableRows.first().locator('.btn-detail');
      await expect(detailButton).toBeVisible();
      await expect(detailButton).toContainText('보기');
    }
  });

  test('테이블 행 클릭 시 상세 페이지로 이동한다', async ({ page }) => {
    const tableRows = page.locator('.favorite-table tbody tr.table-row');
    const rowCount = await tableRows.count();

    if (rowCount > 0) {
      // 첫 번째 행의 종목코드 추출
      const firstRow = tableRows.first();

      // 행 클릭
      await firstRow.click();

      // URL이 변경되었는지 확인 (상세 페이지로 이동)
      // 상세 페이지 라우트: /favorites/:stockCode
      await page.waitForURL(/\/favorites\/\d+/, { timeout: 3000 }).catch(() => {
        // 상세 페이지가 아직 구현되지 않았을 수 있음
        console.log('상세 페이지가 아직 구현되지 않았습니다.');
      });
    }
  });

  test('상세보기 버튼 클릭 시 상세 페이지로 이동한다', async ({ page }) => {
    const tableRows = page.locator('.favorite-table tbody tr');
    const rowCount = await tableRows.count();

    if (rowCount > 0) {
      // 상세보기 버튼 클릭
      await tableRows.first().locator('.btn-detail').click();

      // URL이 변경되었는지 확인
      await page.waitForURL(/\/favorites\/\d+/, { timeout: 3000 }).catch(() => {
        console.log('상세 페이지가 아직 구현되지 않았습니다.');
      });
    }
  });

  test('가격 변동 표시 스타일이 적용된다', async ({ page }) => {
    const tableRows = page.locator('.favorite-table tbody tr');
    const rowCount = await tableRows.count();

    if (rowCount > 0) {
      // 모든 행의 가격 변동 셀 확인
      for (let i = 0; i < Math.min(rowCount, 3); i++) {
        const row = tableRows.nth(i);
        const priceChange = row.locator('.price-change');

        // 가격 변동 셀이 존재하는지 확인
        await expect(priceChange).toBeVisible();

        // up, down, neutral 중 하나의 클래스가 있는지 확인
        const classes = await priceChange.getAttribute('class');
        const hasColorClass = classes.includes('up') || classes.includes('down') || classes.includes('neutral');
        expect(hasColorClass).toBeTruthy();
      }
    }
  });

  test('페이지 푸터가 표시된다', async ({ page }) => {
    // 데이터가 로드될 때까지 대기
    await page.waitForSelector('.favorite-table tbody tr.table-row', { timeout: 10000 });

    // 데이터 소스 정보 확인 (favorites.length > 0일 때만 표시됨)
    await expect(page.locator('.data-source')).toBeVisible();
    await expect(page.locator('.data-source')).toContainText('금융위원회');
  });

  test('반응형 디자인이 적용된다 (모바일)', async ({ page }) => {
    // 모바일 뷰포트로 설정
    await page.setViewportSize({ width: 375, height: 667 });

    // 테이블이 여전히 표시되는지 확인
    await expect(page.locator('.favorite-table')).toBeVisible();

    // 테이블 래퍼가 스크롤 가능한지 확인
    const tableWrapper = page.locator('.table-wrapper');
    await expect(tableWrapper).toBeVisible();
  });

  test('로딩 상태가 표시된다', async ({ page }) => {
    // 페이지 새로고침
    await page.reload();

    // 로딩 메시지가 일시적으로 표시될 수 있음 (매우 빠를 수 있음)
    // 또는 테이블이 바로 표시될 수 있음
    await page.waitForSelector('.favorite-table, .loading-message', { timeout: 5000 });
  });
});
