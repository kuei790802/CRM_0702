import React, { useRef, useState, useEffect } from 'react';
import { ProTable } from '@ant-design/pro-components';
import { Pagination, Tag } from 'antd';
import axios from '../../api/axiosBackend';

const PAGE_SIZE = 10;

const ERPStockLevels = () => {
  const actionRef = useRef();
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);

  const fetchData = async (page = 1) => {
    setLoading(true);
    try {
      const res = await axios.get('/erp/stocklevels', {
        params: { page, pageSize: PAGE_SIZE },
      });
      setData(res.data.data);
      setTotal(res.data.total);
    } catch (error) {
      console.error('庫存明細抓取失敗:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage);
  }, [currentPage]);

  const columns = [
    {
      title: '產品名稱',
      dataIndex: 'productName',
      copyable: true,
    },
    {
      title: '產品編號',
      dataIndex: 'sku',
    },
    {
      title: '倉庫名稱',
      dataIndex: 'warehouse',
    },
    {
      title: '目前庫存',
      dataIndex: 'currentStock',
      render: (_, record) => `${record.currentStock} 件`,
    },
    {
      title: '警戒庫存',
      dataIndex: 'safetyStock',
    },
    {
      title: '狀態',
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: {
        normal: { text: '正常', status: 'Success' },
        low: { text: '低庫存', status: 'Warning' },
        out: { text: '已缺貨', status: 'Error' },
      },
      render: (_, record) => {
        const map = {
          normal: <Tag color="green">正常</Tag>,
          low: <Tag color="orange">低庫存</Tag>,
          out: <Tag color="red">已缺貨</Tag>,
        };
        return map[record.status] || <Tag>未知</Tag>;
      },
    },
  ];

  return (
    <div className="bg-white px-2">
      <ProTable
        columns={columns}
        actionRef={actionRef}
        dataSource={data}
        loading={loading}
        rowKey="id"
        search={{ labelWidth: 'auto' }}
        pagination={false}
        dateFormatter="string"
        headerTitle="庫存明細"
        options={false}
      />

      <div className="flex justify-center py-4">
        <Pagination
          current={currentPage}
          pageSize={PAGE_SIZE}
          total={total}
          onChange={(page) => setCurrentPage(page)}
          showSizeChanger={false}
        />
      </div>
    </div>
  );
};

export default ERPStockLevels;
