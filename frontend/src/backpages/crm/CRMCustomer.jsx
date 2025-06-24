import React, { useRef, useState, useEffect } from 'react';
import { PlusOutlined } from '@ant-design/icons';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Button, Pagination, Space, Tag } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from '../../api/axiosBackend';

const PAGE_SIZE = 10;

const CRMCustomer = () => {
  const actionRef = useRef();
  const [data, setData] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchData = async (page = 1) => {
    setLoading(true);
    try {
      const res = await axios.get('/crm/customers', {
        params: {
          page,
          pageSize: PAGE_SIZE,
        },
      });
      setData(res.data.data);
      setTotal(res.data.total);
    } catch (error) {
      console.error('資料抓取失敗:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData(currentPage);
  }, [currentPage]);

  const columns = [
    {
      title: '客戶名稱',
      dataIndex: 'name',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '聯絡狀態',
      dataIndex: 'status',
      valueType: 'select',
      valueEnum: {
        new: { text: '新客戶', status: 'Default' },
        dealing: { text: '洽談中', status: 'Warning' },
        closed: { text: '成交', status: 'Success' },
      },
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
      title: '建立時間',
      dataIndex: 'created_at',
      valueType: 'date',
      hideInSearch: true,
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (_, record) => [
        <a key="edit" onClick={() => navigate(`/crm/customer/${record.id}`)}>
          編輯
        </a>,
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
        headerTitle="客戶列表"
        options={false}
        toolBarRender={() => [
          <Button
            key="new"
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => navigate('/crm/customer/new')}
          >
            新增客戶
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

export default CRMCustomer;
