import { rest } from 'msw';

// 假資料
let mockCustomers = [
  {
    id: 'c001',
    companyName: '宏達國際股份有限公司',
    contactPerson: '王小明',
    phone: '02-1234-5678',
    location: '台北市中山區',
    tags: ['VIP', '長期合作'],
  },
  {
    id: 'c002',
    companyName: '巨擘科技',
    contactPerson: '林佳蓉',
    phone: '03-4567-8910',
    location: '新竹市東區',
    tags: ['潛力客戶'],
  },
  {
    id: 'c003',
    companyName: '尚達電子',
    contactPerson: '陳志強',
    phone: '07-3456-7890',
    location: '高雄市苓雅區',
    tags: [],
  },
  // 你可繼續擴充更多 mock 資料
];

export const erpCustomersHandler = [
  rest.get('/api/erp/customers', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page')) || 1;
    const pageSize = Number(req.url.searchParams.get('pageSize')) || 10;

    const start = (page - 1) * pageSize;
    const end = start + pageSize;
    const paginatedData = mockCustomers.slice(start, end);

    return res(
      ctx.status(200),
      ctx.json({
        data: paginatedData,
        total: mockCustomers.length,
      })
    );
  }),
];
