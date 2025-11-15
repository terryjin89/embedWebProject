import { useState } from 'react';
import StockAreaChart from '../components/StockAreaChart';

// Mock data for testing (with vs and fltRt fields)
const mockStockData = [
  {
    basDt: "20251111",
    srtnCd: "005930",
    itmsNm: "삼성전자",
    clpr: "103500",
    vs: "2900",
    fltRt: "2.88",
    mkp: "103700",
    hipr: "106000",
    lopr: "102000",
    trqu: "27742542"
  },
  {
    basDt: "20251110",
    srtnCd: "005930",
    itmsNm: "삼성전자",
    clpr: "100600",
    vs: "2700",
    fltRt: "2.76",
    mkp: "98600",
    hipr: "101000",
    lopr: "97900",
    trqu: "23842327"
  },
  {
    basDt: "20251107",
    srtnCd: "005930",
    itmsNm: "삼성전자",
    clpr: "97900",
    vs: "-1300",
    fltRt: "-1.31",
    mkp: "96400",
    hipr: "100300",
    lopr: "96300",
    trqu: "22908083"
  },
  {
    basDt: "20251106",
    srtnCd: "005930",
    itmsNm: "삼성전자",
    clpr: "99200",
    vs: "-1400",
    fltRt: "-1.39",
    mkp: "103700",
    hipr: "103800",
    lopr: "98800",
    trqu: "28655689"
  },
  {
    basDt: "20251105",
    srtnCd: "005930",
    itmsNm: "삼성전자",
    clpr: "100600",
    vs: "-4300",
    fltRt: "-4.1",
    mkp: "101000",
    hipr: "102000",
    lopr: "96700",
    trqu: "44843020"
  }
];

function StockChartTestPage() {
  const [testCase, setTestCase] = useState('normal');
  const [period, setPeriod] = useState(30);

  const handlePeriodChange = (newPeriod) => {
    setPeriod(newPeriod);
    console.log('Period changed to:', newPeriod);
  };

  return (
    <div style={{ padding: '40px', maxWidth: '1200px', margin: '0 auto' }}>
      <h1 style={{ marginBottom: '20px' }}>StockAreaChart 컴포넌트 테스트</h1>

      {/* Test Case Selector */}
      <div style={{ marginBottom: '30px', display: 'flex', gap: '10px' }}>
        <button
          onClick={() => setTestCase('normal')}
          style={{
            padding: '10px 20px',
            backgroundColor: testCase === 'normal' ? '#1890ff' : '#f0f0f0',
            color: testCase === 'normal' ? 'white' : 'black',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer'
          }}
        >
          정상 데이터
        </button>
        <button
          onClick={() => setTestCase('loading')}
          style={{
            padding: '10px 20px',
            backgroundColor: testCase === 'loading' ? '#1890ff' : '#f0f0f0',
            color: testCase === 'loading' ? 'white' : 'black',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer'
          }}
        >
          로딩 상태
        </button>
        <button
          onClick={() => setTestCase('error')}
          style={{
            padding: '10px 20px',
            backgroundColor: testCase === 'error' ? '#1890ff' : '#f0f0f0',
            color: testCase === 'error' ? 'white' : 'black',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer'
          }}
        >
          에러 상태
        </button>
        <button
          onClick={() => setTestCase('nodata')}
          style={{
            padding: '10px 20px',
            backgroundColor: testCase === 'nodata' ? '#1890ff' : '#f0f0f0',
            color: testCase === 'nodata' ? 'white' : 'black',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer'
          }}
        >
          데이터 없음
        </button>
      </div>

      {/* Current Test Case */}
      <div style={{ marginBottom: '20px', padding: '15px', backgroundColor: '#f0f0f0', borderRadius: '4px' }}>
        <strong>현재 테스트 케이스:</strong> {testCase}
      </div>

      {/* Chart Component */}
      {testCase === 'normal' && (
        <StockAreaChart
          data={mockStockData}
          period={period}
          onPeriodChange={handlePeriodChange}
        />
      )}
      {testCase === 'loading' && (
        <StockAreaChart data={[]} loading={true} />
      )}
      {testCase === 'error' && (
        <StockAreaChart data={[]} error="데이터를 불러오는데 실패했습니다." />
      )}
      {testCase === 'nodata' && (
        <StockAreaChart data={[]} />
      )}

      {/* Test Information */}
      <div style={{ marginTop: '40px', padding: '20px', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
        <h3>테스트 항목 (SCRUM-38 + SCRUM-39):</h3>
        <h4>✅ SCRUM-38 - Area Chart 컴포넌트</h4>
        <ul>
          <li>✅ Area Chart 정상 렌더링</li>
          <li>✅ 파란색 그라디언트 효과 적용</li>
          <li>✅ 반응형 디자인 (ResponsiveContainer)</li>
          <li>✅ 애니메이션 효과 (1초 duration)</li>
          <li>✅ 로딩/에러/데이터 없음 상태 표시</li>
        </ul>
        <h4>✅ SCRUM-39 - 커스텀 툴팁 및 기간 선택 UI</h4>
        <ul>
          <li>✅ 커스텀 툴팁 with 전일대비 (hover 시)</li>
          <li>✅ 전일대비 색상 (상승=빨강, 하락=파랑)</li>
          <li>✅ 기간 선택 버튼 (30/60/90일)</li>
          <li>✅ 선택된 버튼 하이라이트</li>
          <li>✅ 주가 상세 정보 카드 (시가/고가/저가/거래량)</li>
        </ul>
        <p style={{ marginTop: '16px', padding: '12px', backgroundColor: '#e6f7ff', borderRadius: '4px', border: '1px solid #91d5ff' }}>
          <strong>현재 선택된 기간:</strong> {period}일
        </p>
      </div>
    </div>
  );
}

export default StockChartTestPage;
