import fuunypic from "../assets/funny.png";

function FunnyError() {
  return (
    <div className="flex flex-col md:flex-row items-center py-16 px-6 md:px-24 bg-yellow-50">
      {/* 左側文字區塊 */}
      <div className="md:w-1/2 mb-10 md:mb-0">
        <h2 className="text-3xl md:text-4xl font-bold text-yellow-800 mb-4">
          型男老闆正在趕工中 🍦
        </h2>
        <p className="text-lg text-yellow-700 leading-relaxed">
          抱歉，這位傳說中做冰淇淋快又帥的老闆目前還沒準備好登場。
          <br />
          他正在後台緊鑼密鼓地研發下一款超人氣口味！
          <br /><br />
          請稍候片刻，或先去吃個甜筒壓壓驚 😎
        </p>
      </div>

      {/* 右側圖片區塊 */}
      <div className="md:w-1/2 text-center">
        <img
          src={fuunypic}
          alt="施工中的型男老闆"
          className="w-full max-w-sm mx-auto rounded-lg shadow-md"
        />
      </div>
    </div>
  );
}

export default FunnyError;
