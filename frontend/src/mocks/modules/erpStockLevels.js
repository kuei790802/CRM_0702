import { rest } from 'msw';

// 模擬庫存明細資料
let mockStockLevels = [
  {
    id: 's001',
    productName: '無線滑鼠',
    sku: 'MOU-001',
    warehouse: '台北倉',
    currentStock: 35,
    safetyStock: 20,
    status: 'normal',
  },
  {
    id: 's002',
    productName: '藍牙耳機',
    sku: 'EAR-002',
    warehouse: '新竹倉',
    currentStock: 8,
    safetyStock: 15,
    status: 'low',
  },
  {
    id: 's003',
    productName: '4K 螢幕',
    sku: 'MON-003',
    warehouse: '高雄倉',
    currentStock: 0,
    safetyStock: 10,
    status: 'out',
  },
  {
    id: 's004',
    productName: '機械鍵盤',
    sku: 'KEY-004',
    warehouse: '台中倉',
    currentStock: 60,
    safetyStock: 30,
    status: 'normal',
  },
  {
    id: 's005',
    productName: '行動電源',
    sku: 'POW-005',
    warehouse: '台北倉',
    currentStock: 4,
    safetyStock: 10,
    status: 'low',
  },
  // ✅ 可再擴充更多資料
];

export const erpStockLevelsHandler = [
  rest.get('/api/erp/stocklevels', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page')) || 1;
    const pageSize = Number(req.url.searchParams.get('pageSize')) || 10;

    const start = (page - 1) * pageSize;
    const end = start + pageSize;
    const paginatedData = mockStockLevels.slice(start, end);

    return res(
      ctx.status(200),
      ctx.json({
        data: paginatedData,
        total: mockStockLevels.length,
      })
    );
  }),
];
