import { useInView } from "react-intersection-observer";
import pic1 from "../../assets/section2.png";

function Section() {
  const { ref, inView } = useInView({
    triggerOnce: true,
    threshold: 0.5,
  });

  return (
    <div
      ref={ref}
      className={`
      flex flex-col md:flex-row items-center py-12 px-4 md:px-16 bg-white
      transition-all duration-[1000ms] ease-in-out
      ${inView ? "opacity-100 translate-y-0" : "opacity-0 translate-y-16"}
     `}
    >
      {/* 左側文字區塊 */}
      <div className="md:w-1/2 mb-8 md:mb-0">
        <p className="text-lg leading-relaxed text-gray-800">
          <strong className="text-logo-tan text-3xl">
            「我們不只是做冰淇淋，我們正在重新定義人們對食物的想像。」
          </strong>
          <br />
          <br />
          他是良野義式冰品的創辦人
          Josh，一位對氣候與飲食未來充滿責任感的手藝職人。
          <br />
          <br />
          頭戴貝雷帽，胸前圍著繡有理念的工作圍裙，Josh
          親手製作每一批冰品，堅持使用
          <strong> 純天然、全素植物原料</strong>
          ，並每日新鮮製作。他說，這不只是口感的堅持，更是對環境與健康的承諾。
          <br />
          <br />
          他以義式冰品為媒介，讓更多人輕鬆體驗 Vegan
          飲食的美好，並傳遞一種「手作與溫度」兼具的生活態度。
        </p>
      </div>

      {/* 右側圖片區塊 */}
      <div className="md:w-1/2 text-center">
        <div className="inline-block max-w-full max-h-[400px]">
          <img
            src={pic1}
            alt="Section Image"
            className="h-auto w-auto max-h-[400px] rounded-lg shadow-lg transition-transform duration-300 transform hover:scale-105"
            loading="lazy"
          />
        </div>
      </div>
    </div>
  );
}

export default Section;
