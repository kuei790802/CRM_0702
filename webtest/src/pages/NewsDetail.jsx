import React from "react";
import img1 from "../assets/new1.png"; 
import { Link } from "react-router-dom";

function NewsDetail() {
  return (
    <div className="max-w-5xl mx-auto px-4 py-10 bg-[#f9f5f0] text-gray-800">
      <h1 className="text-2xl md:text-3xl font-bold text-orange-600 text-center mb-6">
        一枝冰棒，一份心意｜春一枝攜手國北教大實小 為朗島國小英語教育注入希望
      </h1>

      <img
        src={img1}
        alt="news detail"
        className="w-full h-auto rounded-lg shadow mb-6"
      />

      <div className="space-y-4 text-base leading-relaxed">
        <p>
          三月底，國立臺北教育大學附設實驗小學與附設幼兒園師生們發起了一場充滿溫心的公益義賣活動，
          春一枝也參與其中，提供1,500枝天然水果冰棒，讓這場小小市集，匯聚了大大的心意。
          活動當日總營收新台幣24,000元，已全數捐助台東縣蘭嶼朗島國小，作為英語教學教材購置用途，將希望與資源實質帶進偏鄉。
        </p>
        <p>
          春一枝堅持「真實水果、純植製作」的初心，透過文化形式或企業行動冰棒，將所有盈餘全數捐出，
          就是希望透過美味與行動支持社會需求，也讓孩子在甜美滋味中，感受到愛與希望在土地上。
        </p>
        <p>
          活動當天，孩子們不僅參與實體設下小攤，不只是為了賣果，更是透過內容的豐富手繪標語、互動光影。
          三年級鄭妍妤說帶著全班參與其中，學生主張與設計也說出他們：「用我們設計的冰棒和標語，幫國小與國外的夥伴募資，很開心！」。
        </p>
        <p>
          春一枝由衷感謝國北教大實小的老師與孩子們，讓我們的冰棒在推動理念，也讓影響力持續前進，跨越時空。
        </p>
      </div>

      <div className="mt-6 space-y-2 text-sm">
        <p>發佈時間：每一枝冰棒，不只清涼，還讓溫心入心。</p>
        <p>
          延伸閱讀：{" "}
          <a
            href="#"
            className="text-blue-600 underline hover:text-blue-800"
          >
            國北教大實小推廣冰棒
          </a>
          ，
          <a
            href="#"
            className="text-blue-600 underline hover:text-blue-800"
          >
            朗島國小新聞
          </a>
        </p>
        <p>聯絡信箱：info@icespring.com.tw</p>
      </div>

      <div className="text-center mt-10">
        <Link
          to="/news"
          className="inline-block px-6 py-2 bg-orange-500 text-white rounded hover:bg-orange-600 transition"
        >
          返回最新消息
        </Link>
      </div>
    </div>
  );
}

export default NewsDetail;
