<?php
$link=mysql_connect("localhost","kimmije1009","mskimmije1009M") or die("Read DB Fail!");
if(!$link){
      echo " MySQL DB 연결 결과 : <br>\n";
      echo " errno = ".mysql_errno().",<br>\n";
      echo " error = ".mysql_error().",<br>\n";
      echo " link = $link<br>\n";
}
else echo"MySQL DB 열기에 성공하였습니다 !<br>link";

mysql_select_db("kimmije1009",$link);
//$q = "select id, title, name, date, search from board order by id asc";
//$res = mysql_query($q,$link);


if (mysqli_connect_errno($link)){
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

session_start();



$ID = $_POST['ID'];
$PW = $_POST['PW'];

$Q1 = $_POST['Q1'];
$Q2 = $_POST['Q2'];

echo"<br>";

//$ID="TEST3";
//$PW="TEST3";

$TS="TEST2";
//String param = "ID=" + id + "PW=" + pw + "Q1=" + q1 + "Q2=" + q2+"" ;

//////////////////////////////////

$query = mysql_query("INSERT INTO `kimmije1009`.`Chatopia_Member` (`C_INDEX`, `ID`, `PW`,`Q1`,`Q2`) VALUES (NULL, '$ID', '$PW','$Q1','$Q2');"); //성공
$res = mysql_query($query,$link);

if(!$res)
    die("mysql query error");
else
    echo "insert success";

?>
