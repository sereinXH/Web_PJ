document.addEventListener("DOMContentLoaded", function() {
    // 获取按钮元素
    var buttons = document.getElementsByClassName("mainvisual__change__item");
    var mainvisualChangeCurtain = document.querySelector('.mainvisual__change__curtain');
    const mainvisual = document.querySelector('.mainvisual -change'); // 获取包含 "-change" 类的元素

    // 给每个按钮添加点击事件监听器
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].addEventListener("click", function() {
            // 获取按钮的属性值
            var character = this.getAttribute("data-character");
            // 将属性值设置给 body 元素的 data-character 属性
            document.body.setAttribute("data-character", character);

            // 移除其他按钮的 active 类
            for (var j = 0; j < buttons.length; j++) {
                buttons[j].classList.remove("-active");
            }
            // 给当前点击的按钮添加 active 类
            this.classList.add("-active");    
            // mainvisual.classList.toggle('-change'); // 切换 "-change" 类
            // mainvisual.classList.remove('-change');
            mainvisualChangeCurtain.classList.remove("-anim"); // 移除 -anim 类
            void mainvisualChangeCurtain.offsetWidth; // 强制重绘，使得动画重置
            mainvisualChangeCurtain.classList.add("-anim"); // 添加 -anim 类，重新触发动画
            // mainvisual.classList.add('-change');
            // setTimeout(function() {
            //     mainvisualChangeCurtain.classList.remove("-anim");
            // }, 1200);
        });
    }
});

document.addEventListener("DOMContentLoaded", function () {
    // 获取随机数字
    var randomNum = Math.floor(Math.random() * 4) + 1;
    // 设置随机数字给 body 元素的 data-introtheme 属性
    document.body.setAttribute("data-introtheme", randomNum);
});

const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');
// const loginItem = document.getElementById('loginItem');
// const registerItem = document.getElementById('registerItem');

signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
    // loginItem.classList.remove("-active");
    // registerItem.classList.add("-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
    // loginItem.classList.add("-active");
    // registerItem.classList.remove("-active");
});

// 回到起始位置
let footerTop = document.querySelector('.l-footer__top');

footerTop.onclick = function () {
    var top = window.scrollY || document.documentElement.scrollTop;
    scrollBy(0, -top);
}


const signupForm = document.getElementById('signup-form');
const signupNameInput = document.getElementById('signup-name');
const signupEmailInput = document.getElementById('signup-email');
const signupPasswordInput = document.getElementById('signup-password');
const signupPasswordConfirmInput = document.getElementById('signup-passwordconfirm');
const signinForm = document.getElementById('signin-form');
const signinNameInput = document.getElementById('signin-username');
const signinPasswordInput = document.getElementById('signin-password');

// 注册表单提交事件
signupForm.addEventListener('submit', function (event) {
    event.preventDefault(); // 阻止表单的默认提交行为

    // 进行表单验证
    const name = signupNameInput.value;
    const email = signupEmailInput.value;
    const password = signupPasswordInput.value;
    const passwordConfirm = signupPasswordConfirmInput.value;

    // 使用正则表达式进行用户名验证，要求只包含字母、数字、下划线，长度在3到16位
    const nameRegex = /^[a-zA-Z0-9_]{3,16}$/;
    if (!name.match(nameRegex)) {
        alert('用户名格式错误，只允许包含字母、数字、下划线，长度在3到16位');
        return;
    }

    // 使用正则表达式进行邮箱验证
    const emailRegex = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
    if (email.trim() !== '' && !email.match(emailRegex)) {
        alert('邮箱格式错误');
        return;
    }

    // 使用正则表达式进行密码验证，要求包含至少一个小写字母、一个大写字母、一个数字和一个特殊字符，长度在8到16位
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d|[@$!%*?&])[a-zA-Z\d@$!%*?&]{8,16}$/;
    if (!password.match(passwordRegex)) {
        alert('密码格式错误，要求密码至少包含小写字母、大写字母、数字和特殊字符中的任意不重复的两种，长度在8到16位');
        return;
    }

    // 验证密码和确认密码是否一致
    if (password !== passwordConfirm) {
        alert('密码和确认密码不一致');
        return;
    }

    // 进行注册操作
    // TODO: 在这里执行注册逻辑
    const data = new FormData();
    data.append('accountName', name);
    data.append('type', 1);
    data.append('password', password);

    fetch('http://localhost:8080/user/register', {
        method: 'POST',
        body: data
    })
    .then(response => {
        if (response.ok) {
            // 注册成功的处理逻辑
            alert('注册成功！');
            // 可以进行页面跳转或其他操作
        } else {
            // 注册失败的处理逻辑
            response.json().then(errorData => {
                alert(errorData.message); 
            });
        }
    })
    .catch(error => {
        // 网络错误或其他异常的处理逻辑
        console.error('发生错误:', error);
        alert('发生错误，请稍后重试');
    });

    // 注册成功后可以进行页面跳转或其他操作

});

// 登录表单提交事件
signinForm.addEventListener('submit', function (event) {
    event.preventDefault(); // 阻止表单的默认提交行为

    // 进行表单验证
    const name = signinNameInput.value;
    const password = signinPasswordInput.value;

    // 使用正则表达式进行用户名验证，要求只包含字母、数字、下划线，长度在3到16位
    const nameRegex = /^[a-zA-Z0-9_]{3,16}$/;
    if (!name.match(nameRegex)) {
        alert('用户名格式错误，只允许包含字母、数字、下划线，长度在3到16位');
        return;
    }

    // 进行登录操作
    // TODO: 在这里执行登录逻辑
    const data = new FormData();
    data.append('accountName', name);
    data.append('password', password);

    fetch('http://localhost:8080/user/login', {
        method: 'POST',
        body: data
    })
    .then(response => {
        if (response.ok) {
            // 注册成功的处理逻辑
            alert('登录成功！');
            // 可以进行页面跳转或其他操作
        } else {
            // 注册失败的处理逻辑
            response.json().then(errorData => {
                alert(errorData.message); 
            });
        }
    })
    .catch(error => {
        // 网络错误或其他异常的处理逻辑
        console.error('发生错误:', error);
        alert('发生错误，请稍后重试');
    });
    // 登录成功后可以进行页面跳转或其他操作
    window.location.href = "start.html"; 
});


