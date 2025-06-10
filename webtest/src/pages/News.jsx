import NewsSection from "../components/News/NewsSection.jsx";
import img1 from "../assets/new1.png";
import img2 from "../assets/new2.jpg";
import img3 from "../assets/new3.jpg";

const sectionData = [
  {
    id: "news1",
    imageSrc: img1,
    date: "2025/04/17",
    title: "永續食尚｜品味中思考生活與環境的連結",
    content: [
      "『永續食尚』活動於 8 月 2 日在滬出趣人生旅所舉行，強調自家料理與永續飲食概念，讓參與者在品味美食的同時，思考環境與生活的連結。",
      "活動由共時間 Julie 擔任引導員，並邀請了歸維邦 Josh 及吳姍頤作為分享者，透過他們的經驗分享，帶領大家理解永續生活的實踐方式。"
    ]
  },
  {
    id: "news2",
    imageSrc: img2,
    date: "2025/04/17",
    title: "良野進駐蔬食8便利店｜推廣無動物美味選擇",
    content: [
      "良野品牌積極推廣素食文化，近期已進駐台北地區的『蔬食8便利商店』，讓消費者更方便選購無動物性產品的美味選擇。",
      "這項上架消息代表著素食產品正在走入主流生活場域，讓選擇植物性飲食的族群更有支持與資源。"
    ]
  },
  {
    id: "news3",
    imageSrc: img3,
    date: "2025/04/17",
    title: "純植物基冰淇淋｜良野義式冰品成環保飲食新寵",
    content: [
      "良野義式冰淇淋在地創新，主打無奶無蛋的純植物基冰淇淋，成為健康飲食與永續消費的代表，深受年輕族群與關注環保人士喜愛。",
      "品牌創辦人熱情參與各大市集與推廣活動，期望透過冰淇淋這項充滿幸福感的商品，將永續與友善理念融入日常生活。"
    ]
  }
];


function News() {
  return (
    <div className="py-8 bg-[#f9f5f0]">
      <h1 className="text-3xl font-semibold text-center mb-6">最新消息</h1>
      {sectionData.map((section, index) => (
        <NewsSection
          key={index}
          imageSrc={section.imageSrc}
          date={section.date}
          title={section.title}
          id={section.id}
        />
      ))}
    </div>
  );
}

export default News;
