import { rest } from 'msw'

// 模擬使用者資料（使用者身分改為 role）
let mockUsers = [
  {
    id: 'u001',
    name: '王小明',
    email: 'ming.wang@example.com',
    status: 'active',
    role: 'admin',
    created_at: '2025-06-01',
  },
  {
    id: 'u002',
    name: '陳美麗',
    email: 'mei.chen@example.com',
    status: 'inactive',
    role: 'support',
    created_at: '2025-06-10',
  },
  {
    id: 'u003',
    name: '林志玲',
    email: 'chi.ling@example.com',
    status: 'active',
    role: 'sales',
    created_at: '2025-06-15',
  },
  {
    id: 'u004',
    name: '張大海',
    email: 'da.hai@example.com',
    status: 'suspended',
    role: 'guest',
    created_at: '2025-06-18',
  },
  {
    id: 'u005',
    name: '李曉華',
    email: 'hsiao.hua@example.com',
    status: 'active',
    role: 'support',
    created_at: '2025-06-20',
  },
  {
    id: 'u006',
    name: '周子瑜',
    email: 'tzuyu.zhou@example.com',
    status: 'active',
    role: 'sales',
    created_at: '2025-06-21',
  },
]

export const usersManageHandler = [
  // 查詢使用者列表
  rest.get('/api/admin/users', (req, res, ctx) => {
    const page = Number(req.url.searchParams.get('page') || '1')
    const pageSize = Number(req.url.searchParams.get('pageSize') || '10')
    const start = (page - 1) * pageSize
    const end = start + pageSize
    const pageData = mockUsers.slice(start, end)

    return res(
      ctx.status(200),
      ctx.json({
        data: pageData,
        total: mockUsers.length,
      })
    )
  }),

  // 查詢單一使用者
  rest.get('/api/admin/users/:id', (req, res, ctx) => {
    const { id } = req.params
    const user = mockUsers.find((u) => u.id === id)
    if (!user) {
      return res(ctx.status(404), ctx.json({ message: '使用者不存在' }))
    }
    return res(ctx.status(200), ctx.json(user))
  }),

  // 新增使用者
  rest.post('/api/admin/users', async (req, res, ctx) => {
    const body = await req.json()
    const newUser = {
      id: 'u' + Math.random().toString(36).substr(2, 9),
      ...body,
      created_at: new Date().toISOString().split('T')[0],
    }
    mockUsers.unshift(newUser)
    return res(ctx.status(201), ctx.json(newUser))
  }),

  // 刪除使用者
  rest.delete('/api/admin/users/:id', (req, res, ctx) => {
    const { id } = req.params
    mockUsers = mockUsers.filter((user) => user.id !== id)
    return res(ctx.status(204))
  }),
]
