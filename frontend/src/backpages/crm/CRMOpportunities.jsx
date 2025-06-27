import React, { useRef, useState, useEffect } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Button, Pagination, Space, Tag } from 'antd';
import { useNavigate } from 'react-router-dom'; // ✅ 加入 navigate
import axios from '../../api/axiosBackend';

const PAGE_SIZE = 10;

const CRMOpportunities = () => {
  const actionRef = useRef();
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate(); // ✅ 初始化 navigate

  const fetchData = async (page = 1) => {
    setLoading(true);
    try {
      const res = await axios.get('/crm/opportunities', {
        params: { page, pageSize: PAGE_SIZE },
      });
      setData(res.data.data);
      setTotal(res.data.total);
    } catch (error) {
      console.error('商機資料抓取失敗:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage);
  }, [currentPage]);

  const columns = [
    {
      title: '商機名稱',
      dataIndex: 'title',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '客戶名稱',
      dataIndex: 'customerName',
    },
    {
      title: '銷售階段',
      dataIndex: 'stage',
      valueType: 'select',
      valueEnum: {
        prospecting: { text: '潛在開發', status: 'Default' },
        proposal: { text: '已提案', status: 'Processing' },
        negotiating: { text: '議價中', status: 'Warning' },
        won: { text: '成交', status: 'Success' },
        lost: { text: '失敗', status: 'Error' },
      },
    },
    {
      title: '預估金額',
      dataIndex: 'amount',
      render: (_, record) => `$${record.amount.toLocaleString()}`,
    },
    {
      title: '預計成交日',
      dataIndex: 'expectedCloseDate',
      valueType: 'date',
      hideInSearch: true,
    },
    {
      title: '標籤',
      dataIndex: 'tags',
      search: false,
      render: (_, record) => (
        <Space>
          {record.tags.map((tag) => (
            <Tag color="blue" key={tag}>{tag}</Tag>
          ))}
        </Space>
      ),
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (_, record) => [
        <a key="edit" onClick={() => navigate(`/crm/opportunity/${record.id}`)}>編輯</a>, // ✅ 跳轉編輯頁
        <TableDropdown
          key="dropdown"
          menus={[
            { key: 'copy', name: '複製' },
            { key: 'delete', name: '刪除' },
          ]}
        />,
      ],
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
        headerTitle="商機列表"
        options={false}
        toolBarRender={() => [
          <Button
            key="new"
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => navigate('/crm/opportunity/new')} // ✅ 跳轉新增頁
          >
            新增商機
          </Button>,
        ]}
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

export default CRMOpportunities;
