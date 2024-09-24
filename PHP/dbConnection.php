<?php
$ambiente = false;

if ($ambiente) { //Ambiente de Produção

    $HostName = "localhost";
    $HostUser = "";
    $HostPass = "";
    $DatabaseName = "";
}

else { // Ambiente de Desenvolvimento

    $HostName = "localhost";
    $HostUser = "root";
    $HostPass = "";
    $DatabaseName = "echopod_bd";

}
?>
