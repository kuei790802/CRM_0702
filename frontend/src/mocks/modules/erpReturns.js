import { rest } from 'msw';

let mockReturns = [
  {
    id: 'r001',
    returnNo: 'RET-20250610-001',
    customerName: '宏達國際',
    status: 'requested',
    amount: 12300,
    returnDate: '2025-06-10',
  },
  {
    id: 'r002',
    returnNo: 'RET-20250611-002',
    customerName: '尚達電子',
    status: 'approved',
    amount: 5400,
    returnDate: '2025-06-11',
  },
  {
    id: 'r003',
    returnNo: 'RET-20250612-003',
    customerName: '巨擘科技',
    status: 'refunded',
    amount: 8700,
    returnDate: '2025-06-12',
  },
  {
    id: 'r004',
    returnNo: 'RET-20250613-004',
    customerName: '昇陽有限公司',
    status: 'rejected',
    amount: 6400,
    returnDate: '2025-06-13',
  },
  // ✅ 可擴充更多筆資料
];

export const erpReturnsHandler = [
  rest.get('/api/erp/returns', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page')) || 1;
    const pageSize = Number(req.url.searchParams.get('pageSize')) || 10;

    const start = (page - 1) * pageSize;
    const end = start + pageSize;
    const paginatedData = mockReturns.slice(start, end);

    return res(
      ctx.status(200),
      ctx.json({
        data: paginatedData,
        total: mockReturns.length,
      })
    );
  }),
];
