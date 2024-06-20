const express = require('express');
const bodyParser = require('body-parser');
const OpenAI = require('openai');
const cors = require('cors');

const app = express();
app.use(bodyParser.json()); // 解析Json请求体
app.use(cors());

// 配置OpenAI客户端
const client = new OpenAI({
    apiKey: "sk-ZvkbUm7bSKhIOPYIGgioSP4244nxpQpppehNp2MmO22x7VgB",    
    baseURL: "https://api.moonshot.cn/v1",
});

// AI聊天端点
app.post('/ai-chat', async (req, res) => {
    const { message } = req.body;
    try {
        const completion = await client.chat.completions.create({
            model: "moonshot-v1-8k",
            messages: [
                { role: "system", content: "你是 Kimi" },
                { role: "user", content: message }
            ],
            temperature: 0.3
        });
        res.json({ response: completion.choices[0].message.content });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

app.get('/', function (req, res) {
    res.send('<h1>Hello world</h1>');
});

let players = {};

const http = require('http').createServer(app);
const io = require('socket.io')(http, {
    cors: {
        origin: "http://localhost:3001", // 确保此处设置正确
        methods: ["GET", "POST"]
    }
});

io.on('connection', function (socket) {
    console.log('client ' + socket.id + ' connected');

    // 初始化新玩家
    players[socket.id] = {
        socketid: socket.id,
        position: { x: 0, y: 0, z: 0 },
        rotation: { x: 0, y: 0, z: 0 },
        avatar: 'cha1'
    };

    // 广播新玩家信息
    socket.broadcast.emit('newPlayer', players[socket.id]);
    console.log('New player connected: ', players[socket.id]);

    // 发送当前所有玩家信息给新连接的客户端
    socket.emit('currentPlayers', players);

    // 处理玩家移动和旋转
    socket.on('playerMove', function (data) {
        if (players[socket.id]) {
            players[socket.id].position = data.position;
            players[socket.id].rotation = { y: data.rotation.y };
            socket.broadcast.emit('playerMoved', players[socket.id]);
        }
    });

    // 处理玩家更换模型
    socket.on('changeAvatar', function (data) {
        if (players[socket.id]) {
            players[socket.id].avatar = data.avatar;
            socket.broadcast.emit('avatarChanged', players[socket.id]);
        }
    });

    // 处理聊天信息
    socket.on('chat', function (data) {
        io.emit('chat', { message: data.message, socketid: socket.id });
    });

    // 处理玩家断开连接
    socket.on('disconnect', function () {
        console.log('client ' + socket.id + ' disconnected');
        delete players[socket.id];
        socket.broadcast.emit('offline', { socketid: socket.id });
    });
});

http.listen(3000, function () {
    console.log('listening on *:3000');
});
