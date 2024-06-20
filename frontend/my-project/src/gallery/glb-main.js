import * as THREE from 'three';
import { PointerLockControls } from 'three/examples/jsm/controls/PointerLockControls.js';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
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
const avatars = ['Furina', 'Charlotte', 'Chiori', 'Navia'];
avatars.forEach(avatar => {
    const option = document.createElement('option');
    option.value = avatar;
    option.innerText = avatar;
    select.appendChild(option);
});
document.body.appendChild(select);

// 按钮点击事件
button.addEventListener('click', () => {
    select.style.display = 'block';
});

// 下拉菜单选择事件
select.addEventListener('change', () => {
    const selectedAvatar = select.value;
    changeAvatar(`/avatar/${selectedAvatar}.glb`);
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
let sceneModel;
loadModel(scene, (model) => {
    sceneModel = model;
    model.traverse(child => {
        if (child.isMesh) {
            child.geometry.computeBoundingBox();
            console.log(`BoundingBox for ${child.name || child.id}:`, child.geometry.boundingBox);
            child.geometry.boundingBox.applyMatrix4(child.matrixWorld);

            const boxHelper = new THREE.Box3Helper(child.geometry.boundingBox, 0xff0000);
            helperGroup.add(boxHelper);
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
let avatar;
loadAvatar(scene, (loadedAvatar) => {
    avatar = loadedAvatar;
    avatar.rotation.y = Math.PI;
    controlsFirstPerson.getObject().add(avatar);
    avatar.position.set(0, -2, 0); // 使虚拟形象位于相机下方

    avatar.traverse(child => {
        if (child.isMesh) {
            child.geometry.computeBoundingBox();
            console.log(`BoundingBox for ${child.name || child.id}:`, child.geometry.boundingBox);
        }
    });
});

// 切换虚拟形象模型
function changeAvatar(avatarPath) {
    if (avatar) {
        controlsFirstPerson.getObject().remove(avatar);
        scene.remove(avatar);
    }
    loadAvatar(scene, (loadedAvatar) => {
        avatar = loadedAvatar;
        avatar.rotation.y = Math.PI;
        controlsFirstPerson.getObject().add(avatar);
        avatar.position.set(0, -2, 0);

        avatar.traverse(child => {
            if (child.isMesh) {
                child.geometry.computeBoundingBox();
                console.log(`BoundingBox for ${child.name || child.id}:`, child.geometry.boundingBox);
            }
        });
    }, avatarPath);
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

    console.log('Checking collision from', originPoint, 'in direction', directionVector);

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
        console.log('Collision detected!');
        controlsFirstPerson.getObject().position.copy(prevPosition); // 碰撞检测到时恢复之前的位置
        velocity.set(0, 0, 0); // 停止移动
    }

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
