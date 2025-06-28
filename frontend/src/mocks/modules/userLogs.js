import { rest } from 'msw'
import dayjs from 'dayjs'

let mockUserLogs = [
  {
    id: 'log001',
    user: '王小明',
    action: 'login',
    module: '系統登入',
    detail: 'IP: 192.168.1.101',
    timestamp: '2025-06-22T09:00:00',
  },
  {
    id: 'log002',
    user: '陳美麗',
    action: 'update',
    module: '帳號設定',
    detail: '變更密碼',
    timestamp: '2025-06-22T10:15:00',
  },
  {
    id: 'log003',
    user: '王小明',
    action: 'delete',
    module: '客戶資料',
    detail: '刪除 客戶 A',
    timestamp: '2025-06-22T11:30:00',
  },
  {
    id: 'log004',
    user: '林志玲',
    action: 'create',
    module: '新增商機',
    detail: '建立商機：B 公司開發案',
    timestamp: '2025-06-22T13:45:00',
  },
]

export const userLogsHandler = [
  rest.get('/api/admin/user-logs', (req, res, ctx) => {
    const user = req.url.searchParams.get('user')
    const action = req.url.searchParams.get('action')
    const startDate = req.url.searchParams.get('startDate')
    const endDate = req.url.searchParams.get('endDate')

    let filtered = [...mockUserLogs]

    if (user) {
      filtered = filtered.filter((log) =>
        log.user.toLowerCase().includes(user.toLowerCase())
      )
    }

    if (action) {
      filtered = filtered.filter((log) => log.action === action)
    }

    if (startDate && endDate) {
      filtered = filtered.filter((log) => {
        const logTime = dayjs(log.timestamp)
        return (
          logTime.isAfter(dayjs(startDate).startOf('day')) &&
          logTime.isBefore(dayjs(endDate).endOf('day'))
        )
      })
    }

    return res(
      ctx.status(200),
      ctx.json({
        data: filtered,
      })
    )
  }),
]
