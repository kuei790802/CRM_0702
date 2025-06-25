import { useEffect, useState } from 'react'
import axios from '../api/axiosFrontend'
import NewsSection from '../components/News/NewsSection.jsx'



function News() {
  const [newsData, setNewsData] = useState([])

  useEffect(() => {
    axios.get('/news').then((res) => {
      setNewsData(res.data)
    })
  }, [])

  return (
    <div className="py-8">
      <h1 className="text-3xl font-semibold text-center mb-6">最新消息</h1>
      {newsData.map((item, index) => (
        <NewsSection
          key={index}
          imageSrc={item.imageUrl}
          date={item.date}
          title={item.title}
          id={item.id}
        />
      ))}
    </div>
  )
}

export default News
