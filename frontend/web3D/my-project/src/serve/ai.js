const { response } = require("express");   // 解析Json请求体
const OpenAI = require("openai");   //引入OpenAI

// 配置OpenAI客户端
const client = new OpenAI({
    apiKey: "sk-ZvkbUm7bSKhIOPYIGgioSP4244nxpQpppehNp2MmO22x7VgB",    
    baseURL: "https://api.moonshot.cn/v1",
});

// 通过post实现AI
app.post('/chat', async (req, res) => {
  const { message } =req.body;
  try {
    const completion = await client.chat.completions.create({
      model: "moonshot-v1-8k",
      messages: [{
        role: "system", content: "你是 Kimi",
        role: "user", content: message
      }],
      temperature: 0.3
    });
    res.json({ response: completion.choices[0].message.content });
  } catch (error) {
    res.status(500).json({ error: error.message});
  }
});


async function AI_chat(message) {
  try {
    const response = await fetch('http://loacalhost:8080/chat', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ message: message})
    });
    const data = await response.json();
    console.log(data.response);
    reply = data.response;
  } catch (error) {
    console.error('Error:', error);
  }
}