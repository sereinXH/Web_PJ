import * as THREE from 'three';
import { PointerLockControls } from 'three/examples/jsm/controls/PointerLockControls.js';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { io } from 'socket.io-client';
import { loadModel } from './model-loader.js';
import { loadAvatar } from './avatar-loader.js';

// 初始化场景
const scene = new THREE.Scene();

// 调整相机的视角参数 (FOV)
const cameraFirstPerson = new THREE.PerspectiveCamera(50, window.innerWidth / window.innerHeight, 0.1, 1000);
const cameraThirdPerson = new THREE.PerspectiveCamera(50, window.innerWidth / window.innerHeight, 0.1, 1000);
cameraFirstPerson.position.set(0, 2, 0);
cameraThirdPerson.position.set(0, 5, 10);

let currentCamera = cameraFirstPerson;

// 初始化渲染器
const renderer = new THREE.WebGLRenderer();
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);

// 创建一个组来包含所有碰撞对象
const sceneModel = new THREE.Group();
scene.add(sceneModel);

//创建box
const boxGeometry1 = new THREE.BoxGeometry(3, 2, 0.8); 
const boxMaterial1 = new THREE.MeshBasicMaterial({ color: 0x000000 }); 
const box1 = new THREE.Mesh(boxGeometry1, boxMaterial1);
box1.position.set(3.6, 1, 0.5);
sceneModel.add(box1);

const boxGeometry2 = new THREE.BoxGeometry(1, 2, 2); 
const boxMaterial2 = new THREE.MeshBasicMaterial({ color: 0x000000 }); 
const box2 = new THREE.Mesh(boxGeometry2, boxMaterial2);
box2.position.set(11, 1, 3);
sceneModel.add(box2);

const boxGeometry3 = new THREE.BoxGeometry(10, 2, 1); 
const boxMaterial3 = new THREE.MeshBasicMaterial({ color: 0x000000 }); 
const box3 = new THREE.Mesh(boxGeometry3, boxMaterial3);
box3.position.set(5, 1, 5);
sceneModel.add(box3);

const boxGeometry4 = new THREE.BoxGeometry(2, 2, 1); 
const boxMaterial4 = new THREE.MeshBasicMaterial({ color: 0x000000 }); 
const box4 = new THREE.Mesh(boxGeometry4, boxMaterial4);
box4.position.set(-1, 1, 5);
sceneModel.add(box4);

const boxGeometry5 = new THREE.BoxGeometry(2, 2, 4); 
const boxMaterial5 = new THREE.MeshBasicMaterial({ color: 0x000000 }); 
const box5 = new THREE.Mesh(boxGeometry5, boxMaterial5);
box5.position.set(-3.5, 1, 2);
sceneModel.add(box5);

const boxGeometry6 = new THREE.BoxGeometry(2, 2, 2); 
const boxMaterial6 = new THREE.MeshBasicMaterial({ color: 0x000000 }); 
const box6 = new THREE.Mesh(boxGeometry6, boxMaterial6);
box6.position.set(-3.5, 1, -1);
sceneModel.add(box6);

const boxGeometry7 = new THREE.BoxGeometry(2, 2, 2); 
const boxMaterial7 = new THREE.MeshBasicMaterial({ color: 0x000000 }); 
const box7 = new THREE.Mesh(boxGeometry7, boxMaterial7);
box7.position.set(1, 1, -2.5);
sceneModel.add(box7);

const boxGeometry8 = new THREE.BoxGeometry(2, 2, 2); 
const boxMaterial8 = new THREE.MeshBasicMaterial({ color: 0x000000 }); 
const box8 = new THREE.Mesh(boxGeometry8, boxMaterial8);
box8.position.set(-1, 1, -2.5);
sceneModel.add(box8);

const boxGeometry9 = new THREE.BoxGeometry(4, 2, 2); 
const boxMaterial9 = new THREE.MeshBasicMaterial({ color: 0x000000 }); 
const box9 = new THREE.Mesh(boxGeometry9, boxMaterial9);
box9.position.set(8, 1, -2.5);
sceneModel.add(box9);

// 添加按钮和下拉菜单
const button = document.createElement('button');
button.innerText = 'Change Avatar';
button.style.position = 'absolute';
button.style.top = '10px';
button.style.left = '10px';
document.body.appendChild(button);

const select = document.createElement('select');
select.style.position = 'absolute';
select.style.top = '10px';
select.style.left = '100px';
select.style.display = 'none';
const avatars = ['cha1', 'cha2', 'cha3', 'cha4'];
avatars.forEach(avatar => {
    const option = document.createElement('option');
    option.value = avatar;
    option.innerText = avatar;
    select.appendChild(option);
});
document.body.appendChild(select);

// 添加聊天框
const chatInput = document.createElement('input');
chatInput.type = 'text';
chatInput.placeholder = 'Type a message...';
chatInput.style.position = 'absolute';
chatInput.style.bottom = '10px';
chatInput.style.left = '10px';
chatInput.style.width = '200px';
document.body.appendChild(chatInput);

const chatLog = document.createElement('div');
chatLog.style.position = 'absolute';
chatLog.style.bottom = '40px';
chatLog.style.left = '10px';
chatLog.style.width = '200px';
chatLog.style.height = '200px';
chatLog.style.overflowY = 'scroll';
chatLog.style.backgroundColor = 'rgba(0,0,0,0.5)';
chatLog.style.color = 'white';
chatLog.style.padding = '10px';
document.body.appendChild(chatLog);

// WebSocket 连接
const socket = io('http://localhost:3000');

socket.on('connect', () => {
    console.log('Connected to server with ID: ' + socket.id);
});

socket.on('newPlayer', (data) => {
    console.log('New player joined: ', data);
    handleNewPlayer(data);
});

socket.on('currentPlayers', (players) => {
    console.log('Current players: ', players);
    for (let id in players) {
        if (players[id].socketid !== socket.id) {
            handleNewPlayer(players[id]);
        }
    }
});

socket.on('playerMoved', (data) => {
    // console.log('Player moved: ', data);
    handlePlayerMove(data);
});

socket.on('avatarChanged', (data) => {
    // console.log('Player changed avatar: ', data);
    handleAvatarChange(data);
});

socket.on('offline', (data) => {
    console.log('Player disconnected: ', data);
    handlePlayerDisconnect(data);
});

socket.on('chat', (data) => {
    // console.log('Chat message received: ', data);
    handleChatMessage(data);
});

chatInput.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        const message = chatInput.value;
        if (message) {
            socket.emit('chat', { message });
            chatInput.value = '';
        }
    }
});

// 处理玩家数据
const players = new Map();

function handleNewPlayer(data) {
    if (players.has(data.socketid)) return;

    const player = {
        avatar: null,
        mixer: null,
        action: null
    };

    loadAvatar(scene, (loadedAvatar, loadedMixer, loadedAction) => {
        player.avatar = loadedAvatar;
        player.mixer = loadedMixer;
        player.action = loadedAction;
        player.avatar.position.set(data.position.x, data.position.y, data.position.z); // 调整高度
        player.avatar.rotation.set(data.rotation.x, data.rotation.y, data.rotation.z);
        scene.add(player.avatar);
        players.set(data.socketid, player);
    }, `/avatar/${data.avatar}.fbx`, `/anims/walking${avatars.indexOf(data.avatar) + 1}.fbx`);
}

function handlePlayerMove(data) {
    const player = players.get(data.socketid);
    if (player) {
        player.avatar.position.set(data.position.x, data.position.y, data.position.z); // 调整高度
        player.avatar.rotation.set(0, -data.rotation.y, 0);
    }
}

function handleAvatarChange(data) {
    const player = players.get(data.socketid);
    if (player) {
        scene.remove(player.avatar);
        loadAvatar(scene, (loadedAvatar, loadedMixer, loadedAction) => {
            player.avatar = loadedAvatar;
            player.mixer = loadedMixer;
            player.action = loadedAction;
            player.avatar.position.set(data.position.x, data.position.y, data.position.z);
            player.avatar.rotation.set(data.rotation.x, data.rotation.y, data.rotation.z);
            scene.add(player.avatar);
        }, `/avatar/${data.avatar}.fbx`, `/anims/walking${avatars.indexOf(data.avatar) + 1}.fbx`);
    }
}

function handlePlayerDisconnect(data) {
    const player = players.get(data.socketid);
    if (player) {
        scene.remove(player.avatar);
        players.delete(data.socketid);
    }
}

function handleChatMessage(data) {
    const messageElement = document.createElement('div');
    messageElement.textContent = data.message;
    chatLog.appendChild(messageElement);
    chatLog.scrollTop = chatLog.scrollHeight;
}

// 按钮点击事件
button.addEventListener('click', () => {
    select.style.display = 'block';
});

// 下拉菜单选择事件
select.addEventListener('change', () => {
    const selectedAvatar = select.value;
    changeAvatar(`/avatar/${selectedAvatar}.fbx`, `/anims/walking${avatars.indexOf(selectedAvatar) + 1}.fbx`);
    select.style.display = 'none';
});

// 添加光源
const light = new THREE.DirectionalLight(0xffffff, 1);
light.position.set(0, 10, 10).normalize();
scene.add(light);

// 添加环境光
const ambientLight = new THREE.AmbientLight(0x404040);
scene.add(ambientLight);

// 添加辅助工具
const helperGroup = new THREE.Group();
scene.add(helperGroup);

// 加载GLB模型
loadModel(scene, (model) => {
    sceneModel.add(model); // 将加载的模型添加到sceneModel组中
    model.traverse(child => {
        if (child.isMesh) {
            child.geometry.computeBoundingBox();
            //  console.log(`BoundingBox for ${child.name || child.id}:`, child.geometry.boundingBox);
            child.geometry.boundingBox.applyMatrix4(child.matrixWorld);

            // 可视化边界
            // const boxHelper = new THREE.Box3Helper(child.geometry.boundingBox, 0xff0000);
            // helperGroup.add(boxHelper);
        }
    });
});

// 添加PointerLockControls和OrbitControls
const controlsFirstPerson = new PointerLockControls(cameraFirstPerson, document.body);
const controlsThirdPerson = new OrbitControls(cameraThirdPerson, renderer.domElement);
controlsThirdPerson.enablePan = false;
controlsThirdPerson.enableZoom = false;
controlsThirdPerson.target.set(0, 2, 0);
controlsThirdPerson.update();
scene.add(controlsFirstPerson.getObject());

// 加载虚拟形象模型
let avatar, mixer, action;
loadAvatar(scene, (loadedAvatar, loadedMixer, loadedAction) => {
    avatar = loadedAvatar;
    mixer = loadedMixer;
    action = loadedAction;
    avatar.rotation.y = Math.PI;
    controlsFirstPerson.getObject().add(avatar);
    avatar.position.set(0, -2, 0); // 使虚拟形象位于相机下方

    avatar.traverse(child => {
        if (child.isMesh) {
            child.geometry.computeBoundingBox();
            // console.log(`BoundingBox for ${child.name || child.id}:`, child.geometry.boundingBox);
        }
    });
}, '/avatar/cha1.fbx', '/anims/walking1.fbx');

// 切换虚拟形象模型
function changeAvatar(avatarPath, animPath) {
    if (avatar) {
        controlsFirstPerson.getObject().remove(avatar);
        scene.remove(avatar);
    }
    loadAvatar(scene, (loadedAvatar, loadedMixer, loadedAction) => {
        avatar = loadedAvatar;
        mixer = loadedMixer;
        action = loadedAction;
        avatar.rotation.y = Math.PI;
        controlsFirstPerson.getObject().add(avatar);
        avatar.position.set(0, -2, 0);

        avatar.traverse(child => {
            if (child.isMesh) {
                child.geometry.computeBoundingBox();
                // console.log(`BoundingBox for ${child.name || child.id}:`, child.geometry.boundingBox);
            }
        });
    }, avatarPath, animPath);
    socket.emit('changeAvatar', { avatar: select.value });
}

// 控制移动速度和方向
const moveSpeed = 2.0;
const velocity = new THREE.Vector3();
const direction = new THREE.Vector3();
const raycaster = new THREE.Raycaster();
raycaster.far = 1; // 设置Raycaster的检测范围
const keys = {
    forward: false,
    backward: false,
    left: false,
    right: false
};

function handleKeyDown(event) {
    switch (event.code) {
        case 'ArrowUp':
        case 'KeyW':
            keys.forward = true;
            if (action) action.play();
            break;
        case 'ArrowLeft':
        case 'KeyA':
            keys.left = true;
            break;
        case 'ArrowDown':
        case 'KeyS':
            keys.backward = true;
            break;
        case 'ArrowRight':
        case 'KeyD':
            keys.right = true;
            break;
        case 'Space':
            toggleCamera();
            break;
    }
}

function handleKeyUp(event) {
    switch (event.code) {
        case 'ArrowUp':
        case 'KeyW':
            keys.forward = false;
            if (action) action.stop();
            break;
        case 'ArrowLeft':
        case 'KeyA':
            keys.left = false;
            break;
        case 'ArrowDown':
        case 'KeyS':
            keys.backward = false;
            break;
        case 'ArrowRight':
        case 'KeyD':
            keys.right = false;
            break;
    }
}

document.addEventListener('keydown', handleKeyDown);
document.addEventListener('keyup', handleKeyUp);

// 鼠标点击事件监听器
document.addEventListener('click', () => {
    if (currentCamera === cameraFirstPerson) {
        controlsFirstPerson.lock();
    }
});

// 切换相机视角
function toggleCamera() {
    if (currentCamera === cameraFirstPerson) {
        currentCamera = cameraThirdPerson;
        controlsThirdPerson.enabled = true;
    } else {
        currentCamera = cameraFirstPerson;
        controlsThirdPerson.enabled = false;
        controlsFirstPerson.lock();
    }
}

// 更新第三人称相机位置
function updateThirdPersonCamera() {
    if (currentCamera === cameraThirdPerson && avatar) {
        const offset = new THREE.Vector3(0, 0.5, 3);
        offset.applyQuaternion(controlsFirstPerson.getObject().quaternion);
        cameraThirdPerson.position.copy(controlsFirstPerson.getObject().position).add(offset);
        controlsThirdPerson.target.copy(controlsFirstPerson.getObject().position);
        controlsThirdPerson.update();
    }
}


// 检测碰撞函数
function detectCollision(directionVector) {
    if (!sceneModel || !avatar) return [];

    const objectsToCheck = [];
    sceneModel.traverse(child => {
        if (child.isMesh) {
            objectsToCheck.push(child);
        }
    });

    const originPoint = controlsFirstPerson.getObject().position.clone();
    directionVector.applyQuaternion(cameraFirstPerson.quaternion);
    raycaster.set(originPoint, directionVector);

    // console.log('Checking collision from', originPoint, 'in direction', directionVector);

    const intersections = raycaster.intersectObjects(objectsToCheck, true);
    return intersections;
}

// 打印Raycaster检测结果
function printRaycasterResults(directionVector) {
    const intersections = detectCollision(directionVector);
    const originPoint = controlsFirstPerson.getObject().position.clone();
    directionVector.applyQuaternion(cameraFirstPerson.quaternion);

    console.log('Raycaster origin:', originPoint);
    console.log('Raycaster direction:', directionVector);
    console.log('Intersections:', intersections.length > 0 ? intersections : 'No intersections detected.');

    // 添加可视化Raycaster辅助工具
    const rayHelper = new THREE.ArrowHelper(directionVector, originPoint, raycaster.far, 0x00ff00);
    helperGroup.add(rayHelper);
}

// 渲染循环
function animate() {
    requestAnimationFrame(animate);

    const delta = 0.1; // 你可能需要基于时间的 delta 来确保移动速度一致

    velocity.x -= velocity.x * 10.0 * delta;
    velocity.z -= velocity.z * 10.0 * delta;

    direction.z = Number(keys.forward) - Number(keys.backward);
    direction.x = Number(keys.right) - Number(keys.left);
    direction.normalize(); // 这确保了所有方向的移动速度一致

    const prevPosition = controlsFirstPerson.getObject().position.clone();

    if (keys.forward || keys.backward) velocity.z -= direction.z * moveSpeed * delta;
    if (keys.left || keys.right) velocity.x -= direction.x * moveSpeed * delta;

    if (keys.forward || keys.backward || keys.left || keys.right) {
        controlsFirstPerson.moveRight(-velocity.x * delta);
        controlsFirstPerson.moveForward(-velocity.z * delta);

        socket.emit('playerMove', {
            position: { 
                x: controlsFirstPerson.getObject().position.x,
                y: controlsFirstPerson.getObject().position.y - 1.5, // 调整高度
                z: controlsFirstPerson.getObject().position.z
            },
            rotation: {
                // x: controlsFirstPerson.getObject().rotation.x,
                y: controlsFirstPerson.getObject().rotation.y,
                // z: controlsFirstPerson.getObject().rotation.z
            }
        });

        // console.log('Emitting playerMove: ', {
        //     position: controlsFirstPerson.getObject().position,
        //     rotation: {
        //         x: controlsFirstPerson.getObject().rotation.x,
        //         y: controlsFirstPerson.getObject().rotation.y,
        //         z: controlsFirstPerson.getObject().rotation.z
        //     }
        // });
    }

    let collisionDetected = false;

    if (keys.forward) {
        collisionDetected = detectCollision(new THREE.Vector3(0, 0, -1)).length > 0;
    } else if (keys.backward) {
        collisionDetected = detectCollision(new THREE.Vector3(0, 0, 1)).length > 0;
    } else if (keys.left) {
        collisionDetected = detectCollision(new THREE.Vector3(-1, 0, 0)).length > 0;
    } else if (keys.right) {
        collisionDetected = detectCollision(new THREE.Vector3(1, 0, 0)).length > 0;
    }

    if (collisionDetected) {
        // console.log('Collision detected!');
        controlsFirstPerson.getObject().position.copy(prevPosition); // 碰撞检测到时恢复之前的位置
        velocity.set(0, 0, 0); // 停止移动
    }

    if (mixer) mixer.update(delta);

    updateThirdPersonCamera(); // 更新第三人称相机位置
    renderer.render(scene, currentCamera);
}
animate();

// 调整窗口大小
window.addEventListener('resize', function() {
    cameraFirstPerson.aspect = window.innerWidth / window.innerHeight;
    cameraThirdPerson.aspect = window.innerWidth / window.innerHeight;
    cameraFirstPerson.updateProjectionMatrix();
    cameraThirdPerson.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});
