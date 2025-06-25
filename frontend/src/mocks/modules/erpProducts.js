import { rest } from 'msw';

// 模擬產品資料
let mockProducts = [
  {
    id: 'p001',
    productName: '無線滑鼠',
    sku: 'MOU-001',
    category: '周邊設備',
    stock: 128,
    status: 'active',
  },
  {
    id: 'p002',
    productName: '藍牙耳機',
    sku: 'EAR-002',
    category: '音訊設備',
    stock: 5,
    status: 'lowstock',
  },
  {
    id: 'p003',
    productName: '4K 螢幕',
    sku: 'MON-003',
    category: '顯示器',
    stock: 0,
    status: 'inactive',
  },
  {
    id: 'p004',
    productName: '機械鍵盤',
    sku: 'KEY-004',
    category: '周邊設備',
    stock: 57,
    status: 'active',
  },
  {
    id: 'p005',
    productName: '行動電源',
    sku: 'POW-005',
    category: '充電設備',
    stock: 9,
    status: 'lowstock',
  },
  // ✅ 可自行增加更多產品資料
];

export const erpProductsHandler = [
  rest.get('/api/erp/products', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page')) || 1;
    const pageSize = Number(req.url.searchParams.get('pageSize')) || 10;

    const start = (page - 1) * pageSize;
    const end = start + pageSize;
    const paginatedData = mockProducts.slice(start, end);

    return res(
      ctx.status(200),
      ctx.json({
        data: paginatedData,
        total: mockProducts.length,
      })
    );
  }),
];
