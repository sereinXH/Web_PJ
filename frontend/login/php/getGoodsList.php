<?php
$page = $_GET["page"];
header('content-type:text/html;charset=utf-8');
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "test";

//实例化mysqli对象，连接mysql数据库
$mysqli = new mysqli($servername, $username, $password, $dbname);

if ($mysqli->connect_errno) {
    die($mysqli->connect_error);
}
$mysqli->set_charset('utf8'); //设置字符集

showGoods($mysqli,$page);

$mysqli->close();

function showGoods($mysqli,$page)
{
    $num = $page * 20;
    $sql = "SELECT * FROM goods limit $num, 20";
    $result = mysqli_query($mysqli, $sql);
    $arr = mysqli_fetch_all($result,MYSQLI_ASSOC);

    echo json_encode(array("error" => 0,"data" => $arr));
}
?>