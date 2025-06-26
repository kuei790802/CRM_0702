import { rest } from 'msw';

let mockOrders = [
  {
    id: 'o001',
    orderNo: 'ORD-20250601-001',
    customerName: '宏達國際',
    status: 'pending',
    totalAmount: 32800,
    createdAt: '2025-06-01',
  },
  {
    id: 'o002',
    orderNo: 'ORD-20250603-002',
    customerName: '巨擘科技',
    status: 'processing',
    totalAmount: 19800,
    createdAt: '2025-06-03',
  },
  {
    id: 'o003',
    orderNo: 'ORD-20250605-003',
    customerName: '尚達電子',
    status: 'shipped',
    totalAmount: 7650,
    createdAt: '2025-06-05',
  },
  {
    id: 'o004',
    orderNo: 'ORD-20250608-004',
    customerName: '昇陽有限公司',
    status: 'canceled',
    totalAmount: 14200,
    createdAt: '2025-06-08',
  },
  // ✅ 你可繼續擴充更多資料
];

export const erpOrdersHandler = [
  rest.get('/api/erp/orders', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page')) || 1;
    const pageSize = Number(req.url.searchParams.get('pageSize')) || 10;

    const start = (page - 1) * pageSize;
    const end = start + pageSize;
    const paginatedData = mockOrders.slice(start, end);

    return res(
      ctx.status(200),
      ctx.json({
        data: paginatedData,
        total: mockOrders.length,
      })
    );
  }),
];
