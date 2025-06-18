// src/mocks/handlers/crmCustomerHandler.js
import { rest } from 'msw';

// ✅ 產生模擬 10 筆資料（第 5 頁：id 41~50）
const mockCRMData = Array.from({ length: 10 }, (_, i) => {
  const id = 41 + i;
  return {
    id,
    name: `客戶 ${id}`,
    status: ['new', 'dealing', 'closed'][id % 3],
    tags: id % 2 === 0 ? ['重要'] : ['潛在客戶'],
    created_at: `2025-06-${(id % 30) + 1}`.padStart(10, '0'),
  };
});

export const crmCustomerHandlers = [
  rest.get('/api/crm/customers', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page')) || 5;
    const pageSize = Number(req.url.searchParams.get('pageSize')) || 10;

    // ✅ 總共 100 筆，但只模擬第 5 頁
    return res(
      ctx.status(200),
      ctx.json({
        data: mockCRMData,
        total: 100,
        page,
        pageSize,
      })
    );
  }),
];
