//webtest1\src\components\Home\Section.jsx
import pic1 from '../../assets/section1.jpg'; // 圖片路徑

function Section() {
  return (
    <div className="flex flex-col md:flex-row items-center py-12 px-4 md:px-16 bg-white">
      {/* 左側文字區塊 */}
      <div className="md:w-1/2 mb-8 md:mb-0">
        <p className="text-lg leading-relaxed text-gray-800">
          <strong className="text-blue-600">「做冰淇淋，不只是工作，是一種生活態度。」</strong>
          <br /><br />
          這位被譽為「冰界型男」的老闆，接受專訪時侃侃而談。
          從義式冰淇淋到純素冰淇淋，他堅持 <strong>每日新鮮現做、絕不添加人工香料</strong>。
          <br /><br />
          「我們做的不是甜點，而是讓人微笑的魔法。」
          他說，目標是讓每位客人都能在烈日下，找到一球專屬自己的快樂。
        </p>
      </div>

      {/* 右側圖片區塊 */}
      <div className="md:w-1/2 text-center">
        <img
            src={pic1}
            alt="Section Image"
            className="w-full h-auto rounded-lg shadow-lg transition-transform duration-300 transform hover:scale-105"
            loading="lazy"
            width="500"
            height="500"
        />
      </div>
    </div>
  );
}

export default Section;
