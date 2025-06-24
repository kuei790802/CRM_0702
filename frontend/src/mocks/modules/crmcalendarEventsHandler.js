import { rest } from 'msw';
import dayjs from 'dayjs';

// ✅ 擴充事件資料：跨月份 + 同日多事件
const calendarEvents = [
  {
    date: '2025-06-19',
    title: '與客戶會議',
    type: 'success',
    content: '地點：台北總部，參與人：張經理',
    time: '10:00 - 11:00',
  },
  {
    date: '2025-06-19',
    title: '內部專案檢討',
    type: 'warning',
    content: '討論本月進度與下月規劃',
    time: '13:30 - 14:30',
  },
  {
    date: '2025-06-19',
    title: '技術顧問會議',
    type: 'success',
    content: '與顧問進行 CRM 技術對接討論',
    time: '16:00 - 17:00',
  },
  {
    date: '2025-06-20',
    title: '付款失敗提醒',
    type: 'error',
    content: '客戶 B 的付款交易失敗，請跟進',
    time: '全天',
  },
  {
    date: '2025-07-03',
    title: '產品簡報會議',
    type: 'warning',
    content: '準備 7 月新品簡報，客戶 C 參與',
    time: '15:00 - 16:30',
  },
  {
    date: '2025-07-15',
    title: 'ERP 教育訓練',
    type: 'success',
    content: '新進員工 ERP 操作教學',
    time: '09:00 - 12:00',
  },
  {
    date: '2025-07-15',
    title: '主管月會',
    type: 'warning',
    content: '部門主管月度報告與預算規劃',
    time: '14:00 - 16:00',
  },
  {
    date: '2025-08-01',
    title: '季度銷售檢討',
    type: 'error',
    content: 'Q2 銷售績效未達標，擬定改善對策',
    time: '上午',
  },
  {
    date: '2025-08-10',
    title: '系統升級通知',
    type: 'warning',
    content: 'CRM 系統預計於當日凌晨維護',
    time: '00:00 - 04:00',
  },
];

export const crmcalendarEventsHandler = [
  rest.get('/api/crmcalendar-events', (req, res, ctx) => {
    const year = req.url.searchParams.get('year');
    const month = req.url.searchParams.get('month');

    if (!year || !month) {
      return res(ctx.status(400), ctx.json({ error: 'Missing year or month' }));
    }

    const filtered = calendarEvents.filter((event) => {
      const eventDate = dayjs(event.date);
      return (
        eventDate.year() === parseInt(year) &&
        eventDate.month() + 1 === parseInt(month)
      );
    });

    return res(ctx.status(200), ctx.json(filtered));
  }),
];
