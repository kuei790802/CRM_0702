// src/mocks/handlers/crmOpportunitiesHandler.js
import { rest } from 'msw';

// ✅ 模擬第 5 頁的 10 筆商機資料（id 41~50）
const mockOpportunities = Array.from({ length: 10 }, (_, i) => {
  const id = 41 + i;
  return {
    id,
    title: `商機 ${id}`,
    customerName: `客戶 ${id}`,
    stage: ['prospecting', 'proposal', 'negotiating', 'won', 'lost'][id % 5],
    amount: 10000 + id * 500,
    expectedCloseDate: `2025-07-${(id % 30 + 1).toString().padStart(2, '0')}`,
    tags: id % 2 === 0 ? ['重要'] : ['潛在'],
  };
});

export const crmOpportunitiesHandlers = [
  rest.get('/api/crm/opportunities', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page')) || 5;
    const pageSize = Number(req.url.searchParams.get('pageSize')) || 10;

    return res(
      ctx.status(200),
      ctx.json({
        data: mockOpportunities,
        total: 100, // ✅ 模擬共 100 筆，10 頁
        page,
        pageSize,
      })
    );
  }),
];
