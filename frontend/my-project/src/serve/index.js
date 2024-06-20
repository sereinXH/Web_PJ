var express = require('express');
var app = express();
var http = require('http').createServer(app);
var io = require('socket.io')(http, {
    cors: {
        origin: "http://localhost:3001", // 确保此处设置正确
        methods: ["GET", "POST"]
    }
});

app.get('/', function (req, res) {
    res.send('<h1>Hello world</h1>');
});

let players = {};

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
        // console.log('Received playerMove from client ' + socket.id + ': ', data);
        if (players[socket.id]) {
            players[socket.id].position = data.position;
            players[socket.id].rotation = { y: data.rotation.y };
            socket.broadcast.emit('playerMoved', players[socket.id]);
            // console.log('Player moved: ', players[socket.id]);
        }
    });
    

    // 处理玩家更换模型
    socket.on('changeAvatar', function (data) {
        // console.log('Received changeAvatar from client ' + socket.id + ': ', data);
        if (players[socket.id]) {
            players[socket.id].avatar = data.avatar;
            socket.broadcast.emit('avatarChanged', players[socket.id]);
            // console.log('Player changed avatar: ', players[socket.id]);
        }
    });

    // 处理聊天信息
    socket.on('chat', function (data) {
        // console.log('Received chat from client ' + socket.id + ': ', data);
        io.emit('chat', { message: data.message, socketid: socket.id });
        // console.log('Chat message from ' + socket.id + ': ' + data.message);
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