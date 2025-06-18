import React, { useEffect, useState } from 'react';
import { Calendar, Select, Button, Badge, Modal, Tooltip } from 'antd';
import dayjs from 'dayjs';
import axios from '../../api/axiosInstance'; // ✅ 你已設定好的 axios instance

const { Option } = Select;

const CRMCalendar = () => {
  const [current, setCurrent] = useState(dayjs());
  const [selectedDate, setSelectedDate] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [eventsData, setEventsData] = useState({}); // key: date, value: event[]

  const currentYear = dayjs().year();
  const years = Array.from({ length: currentYear - 2015 + 1 }, (_, i) => 2015 + i);
  const months = Array.from({ length: 12 }, (_, i) => i);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios.get('/crmcalendar-events', {
          params: {
            year: current.year(),
            month: current.month() + 1, // dayjs 是 0-based month
          },
        });
        const grouped = {};
        res.data.forEach((event) => {
          const date = dayjs(event.date).format('YYYY-MM-DD');
          if (!grouped[date]) grouped[date] = [];
          grouped[date].push(event);
        });
        setEventsData(grouped);
      } catch (error) {
        console.error('載入行事曆事件失敗：', error);
      }
    };
    fetchData();
  }, [current.year(), current.month()]);

  const getListData = (value) => {
    const key = value.format('YYYY-MM-DD');
    return eventsData[key] || [];
  };

  const dateCellRender = (value) => {
    const list = getListData(value);
    const hasEvent = list.length > 0;

    const content = (
      <ul className="space-y-0.5">
        {list.map((item, index) => (
          <li key={index}>
            <Badge status={item.type} text={item.title} />
          </li>
        ))}
      </ul>
    );

    return hasEvent ? (
      <Tooltip title="雙擊可查看詳細內容" placement="top">
        <div
          onDoubleClick={() => {
            setSelectedDate({
              date: value.format('YYYY-MM-DD'),
              events: list,
            });
            setModalVisible(true);
          }}
          className="cursor-pointer px-1"
        >
          {content}
        </div>
      </Tooltip>
    ) : (
      content
    );
  };

  const customHeader = ({ value, onChange }) => (
    <div className="flex items-center justify-between px-4 py-2 border-b border-gray-200 bg-white">
      <div className="text-base font-semibold text-gray-700">
        {value.format('YYYY 年 MM 月')}
      </div>

      <div className="flex items-center gap-2">
        <Select
          value={value.year()}
          onChange={(val) => onChange(value.clone().year(val))}
          size="middle"
          className="w-28 text-base"
        >
          {years.map((y) => (
            <Option key={y} value={y}>
              {y}年
            </Option>
          ))}
        </Select>

        <Select
          value={value.month()}
          onChange={(val) => onChange(value.clone().month(val))}
          size="middle"
          className="w-24 text-base"
        >
          {months.map((m) => (
            <Option key={m} value={m}>
              {m + 1}月
            </Option>
          ))}
        </Select>

        <Button
          type="default"
          size="middle"
          className="text-base px-4"
          onClick={() => onChange(dayjs())}
        >
          今天
        </Button>
      </div>
    </div>
  );

  return (
    <div className="p-4 bg-white">
      <Calendar
        value={current}
        onChange={setCurrent}
        cellRender={dateCellRender}
        headerRender={customHeader}
      />

      <Modal
        open={modalVisible}
        title={`事件詳情：${selectedDate?.date}`}
        footer={null}
        onCancel={() => setModalVisible(false)}
      >
        <ul className="space-y-2">
          {selectedDate?.events.map((event, index) => (
            <li key={index} className="flex flex-col gap-1 border-b pb-2">
              <div className="flex items-center gap-2">
                <Badge status={event.type} />
                <span className="font-semibold">{event.title}</span>
              </div>
              <div className="text-gray-600 text-sm">{event.content}</div>
              <div className="text-gray-500 text-xs">時間：{event.time}</div>
            </li>
          ))}
        </ul>
      </Modal>
    </div>
  );
};

export default CRMCalendar;
