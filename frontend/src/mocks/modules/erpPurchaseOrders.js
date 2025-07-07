import { rest } from 'msw';

// 模擬進貨單資料
let mockPurchaseOrders = [
  {
    purchaseOrderId: 1,
    orderNumber: 'PO-20250615-001',
    supplierId: 1,
    supplierName: '鴻運電子材料行',
    status: 'RECEIVED',
    totalAmount: 25400,
    orderDate: '2025-06-15',
    currency: 'TWD',
    remarks: '第一季度備貨',
    details: [
      { 
        id: 1, 
        productId: 1, 
        productName: '無線滑鼠', 
        quantity: 20, 
        unitPrice: 400, 
        warehouseId: 1, 
        warehouseName: '主倉庫' 
      },
      { 
        id: 2, 
        productId: 2, 
        productName: '鍵盤', 
        quantity: 10, 
        unitPrice: 600, 
        warehouseId: 1, 
        warehouseName: '主倉庫' 
      },
    ],
  },
  {
    purchaseOrderId: 2,
    orderNumber: 'PO-20250616-002',
    supplierId: 2,
    supplierName: '全勝電子',
    status: 'ORDERED',
    totalAmount: 17200,
    orderDate: '2025-06-16',
    currency: 'TWD',
    remarks: '緊急補貨',
    details: [
      { 
        id: 3, 
        productId: 3, 
        productName: '藍牙耳機', 
        quantity: 15, 
        unitPrice: 500, 
        warehouseId: 2, 
        warehouseName: '分倉庫' 
      },
      { 
        id: 4, 
        productId: 4, 
        productName: '充電器', 
        quantity: 8, 
        unitPrice: 700, 
        warehouseId: 2, 
        warehouseName: '分倉庫' 
      },
    ],
  },
  {
    purchaseOrderId: 3,
    orderNumber: 'PO-20250617-003',
    supplierId: 3,
    supplierName: '富昌電器',
    status: 'DRAFT',
    totalAmount: 8800,
    orderDate: '2025-06-17',
    currency: 'TWD',
    remarks: '',
    details: [
      { 
        id: 5, 
        productId: 5, 
        productName: '延長線', 
        quantity: 20, 
        unitPrice: 220, 
        warehouseId: 1, 
        warehouseName: '主倉庫' 
      },
    ],
  },
];

// 模擬供應商資料
const mockSuppliers = [
  { supplierId: 1, name: '鴻運電子材料行' },
  { supplierId: 2, name: '全勝電子' },
  { supplierId: 3, name: '富昌電器' },
];

// 模擬產品資料
const mockProducts = [
  { productId: 1, name: '無線滑鼠' },
  { productId: 2, name: '鍵盤' },
  { productId: 3, name: '藍牙耳機' },
  { productId: 4, name: '充電器' },
  { productId: 5, name: '延長線' },
];

// 模擬倉庫資料
const mockWarehouses = [
  { warehouseId: 1, name: '主倉庫' },
  { warehouseId: 2, name: '分倉庫' },
];

export const erpPurchaseOrdersHandler = [
  // 獲取進貨單列表
  rest.get('/api/purchaseOrders', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page')) || 0;
    const pageSize = Number(req.url.searchParams.get('size')) || 10;
    const keyword = req.url.searchParams.get('keyword');
    const status = req.url.searchParams.get('status');

    let filteredData = mockPurchaseOrders;

    // 關鍵字搜尋
    if (keyword) {
      filteredData = filteredData.filter(order => 
        order.supplierName.toLowerCase().includes(keyword.toLowerCase()) ||
        order.orderNumber.toLowerCase().includes(keyword.toLowerCase())
      );
    }

    // 狀態篩選
    if (status) {
      filteredData = filteredData.filter(order => order.status === status);
    }

    const start = page * pageSize;
    const end = start + pageSize;
    const paginatedData = filteredData.slice(start, end);

    return res(
      ctx.status(200),
      ctx.json({
        content: paginatedData,
        totalElements: filteredData.length,
        totalPages: Math.ceil(filteredData.length / pageSize),
        size: pageSize,
        number: page,
      })
    );
  }),

  // 獲取單個進貨單
  rest.get('/api/purchaseOrders/:id', (req, res, ctx) => {
    const { id } = req.params;
    const order = mockPurchaseOrders.find(o => o.purchaseOrderId === parseInt(id));
    
    if (!order) {
      return res(ctx.status(404), ctx.json({ message: '進貨單未找到' }));
    }

    return res(ctx.status(200), ctx.json(order));
  }),

  // 建立進貨單
  rest.post('/api/purchaseOrders', (req, res, ctx) => {
    const newOrder = {
      purchaseOrderId: mockPurchaseOrders.length + 1,
      orderNumber: `PO-${new Date().toISOString().slice(0, 10).replace(/-/g, '')}-${String(mockPurchaseOrders.length + 1).padStart(3, '0')}`,
      status: 'DRAFT',
      ...req.body,
    };

    mockPurchaseOrders.push(newOrder);
    
    return res(ctx.status(201), ctx.json(newOrder));
  }),

  // 更新進貨單
  rest.put('/api/purchaseOrders/:id', (req, res, ctx) => {
    const { id } = req.params;
    const index = mockPurchaseOrders.findIndex(o => o.purchaseOrderId === parseInt(id));
    
    if (index === -1) {
      return res(ctx.status(404), ctx.json({ message: '進貨單未找到' }));
    }

    mockPurchaseOrders[index] = {
      ...mockPurchaseOrders[index],
      ...req.body,
    };

    return res(ctx.status(200), ctx.json(mockPurchaseOrders[index]));
  }),

  // 刪除進貨單
  rest.delete('/api/purchaseOrders/:id', (req, res, ctx) => {
    const { id } = req.params;
    const index = mockPurchaseOrders.findIndex(o => o.purchaseOrderId === parseInt(id));
    
    if (index === -1) {
      return res(ctx.status(404), ctx.json({ message: '進貨單未找到' }));
    }

    const deletedOrder = mockPurchaseOrders[index];
    deletedOrder.status = 'CANCELLED';

    return res(ctx.status(200), ctx.json(deletedOrder));
  }),

  // 獲取供應商列表
  rest.get('/api/suppliers', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mockSuppliers));
  }),

  // 獲取產品列表
  rest.get('/api/products', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mockProducts));
  }),

  // 獲取倉庫列表
  rest.get('/api/warehouses', (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mockWarehouses));
  }),
];