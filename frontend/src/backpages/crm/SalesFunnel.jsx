import React, { useState, useEffect } from "react";
import { Button, Modal, message, Descriptions } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import axios from "../../api/axiosBackend";
import CRMOpportunityForm from "./CRMOpportunityForm";
import SalesFunnelBoard from "../../backcomponents/crm/SalesFunnelBoard.jsx";

export default function SalesFunnel() {
  const [columns, setColumns] = useState({});
  const [modalOpen, setModalOpen] = useState(false);
  const [detailModalOpen, setDetailModalOpen] = useState(false);
  const [selectedOpportunity, setSelectedOpportunity] = useState(null);

  useEffect(() => {
    const fetchFunnelData = async () => {
      try {
        const res = await axios.get("/opportunities/funnel");

        const funnelData = {};
        res.data.forEach((stageItem) => {
          const key = stageItem.stageDisplayName;

          funnelData[key] = stageItem.opportunities.map((op) => {
            const tags = op.tags ?? [];
            const type = tags[0]?.tagName || "default"; // ✅ 取第一個 tagName 為 type

            return {
              id: `c${op.opportunityId}`,
              title: op.opportunityName,
              rating: Math.round(op.averageRating ?? 0),
              type,
              tags,
              ...op,
            };
          });
        });

        setColumns(funnelData);
      } catch (error) {
        console.error("載入商機漏斗失敗：", error);
        message.error("無法載入商機資料");
      }
    };

    fetchFunnelData();
  }, []);

  const handleCreate = (formValues) => {
    const id = `c${Date.now()}`;
    const stageKey = formValues.stage ?? "INITIAL_CONTACT";

    const tags = formValues.tags ?? [];
    const type = tags[0]?.tagName || "default";

    const newOpportunity = {
      id,
      title: formValues.opportunityName,
      rating: Math.round(formValues.averageRating ?? 1),
      type,
      tags,
      ...formValues,
    };

    setColumns((prev) => ({
      ...prev,
      [stageKey]: [...(prev[stageKey] || []), newOpportunity],
    }));

    setModalOpen(false);
  };

  const handleCardDoubleClick = (opportunity) => {
    console.log("雙擊商機卡片:", opportunity);
    setSelectedOpportunity(opportunity);
    setDetailModalOpen(true);
  };

  return (
    <div className="p-4">
      <div className="flex justify-end items-center mb-4">
        <Button
          type="primary"
          size="large"
          icon={<PlusOutlined />}
          onClick={() => setModalOpen(true)}
        >
          新增商機
        </Button>
      </div>

      <SalesFunnelBoard
        columns={columns}
        setColumns={setColumns}
        onCardDoubleClick={handleCardDoubleClick}
      />

      <Modal
        open={modalOpen}
        onCancel={() => setModalOpen(false)}
        footer={null}
        title="新增商機"
        destroyOnClose
      >
        <CRMOpportunityForm onSubmit={handleCreate} />
      </Modal>

      <Modal
        title="商機詳情"
        open={detailModalOpen}
        onCancel={() => setDetailModalOpen(false)}
        footer={null}
      >
        {selectedOpportunity && (
          <Descriptions column={1} bordered size="small">
            <Descriptions.Item label="商機名稱">
              {selectedOpportunity.opportunityName}
            </Descriptions.Item>
            <Descriptions.Item label="預估金額">
              ${selectedOpportunity.expectedValue?.toLocaleString()}
            </Descriptions.Item>
            <Descriptions.Item label="說明">
              {selectedOpportunity.description || "-"}
            </Descriptions.Item>
            <Descriptions.Item label="成交日">
              {selectedOpportunity.closeDate}
            </Descriptions.Item>
            <Descriptions.Item label="階段">
              {selectedOpportunity.stage}
            </Descriptions.Item>
            <Descriptions.Item label="客戶">
              {selectedOpportunity.customerName}
            </Descriptions.Item>
            <Descriptions.Item label="聯絡人">
              {selectedOpportunity.contactName || "-"}
            </Descriptions.Item>
          </Descriptions>
        )}
      </Modal>
    </div>
  );
}
