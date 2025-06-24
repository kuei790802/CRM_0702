import { rest } from 'msw';

// 模擬進貨單資料，包含 details
let mockPurchaseOrders = [
  {
    id: 'po001',
    poNumber: 'PO-20250615-001',
    supplierName: '鴻運電子材料行',
    status: 'received',
    totalAmount: 25400,
    purchaseDate: '2025-06-15',
    details: [
      { productName: '無線滑鼠', quantity: 20, unitPrice: 400 },
      { productName: '鍵盤', quantity: 10, unitPrice: 600 },
    ],
  },
  {
    id: 'po002',
    poNumber: 'PO-20250616-002',
    supplierName: '全勝電子',
    status: 'ordered',
    totalAmount: 17200,
    purchaseDate: '2025-06-16',
    details: [
      { productName: '藍牙耳機', quantity: 15, unitPrice: 500 },
      { productName: '充電器', quantity: 8, unitPrice: 700 },
    ],
  },
  {
    id: 'po003',
    poNumber: 'PO-20250617-003',
    supplierName: '富昌電器',
    status: 'draft',
    totalAmount: 8800,
    purchaseDate: '2025-06-17',
    details: [
      { productName: '延長線', quantity: 20, unitPrice: 220 },
    ],
  },
  {
    id: 'po004',
    poNumber: 'PO-20250618-004',
    supplierName: '大宏倉儲',
    status: 'canceled',
    totalAmount: 13800,
    purchaseDate: '2025-06-18',
    details: [
      { productName: 'LED 燈泡', quantity: 50, unitPrice: 120 },
      { productName: '節能燈管', quantity: 20, unitPrice: 190 },
    ],
  },
  // ✅ 可依需求擴充更多資料
];

export const erpPurchaseOrdersHandler = [
  rest.get('/api/erp/purchaseorders', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page')) || 1;
    const pageSize = Number(req.url.searchParams.get('pageSize')) || 10;

    const start = (page - 1) * pageSize;
    const end = start + pageSize;
    const paginatedData = mockPurchaseOrders.slice(start, end);

    return res(
      ctx.status(200),
      ctx.json({
        data: paginatedData,
        total: mockPurchaseOrders.length,
      })
    );
  }),
];
