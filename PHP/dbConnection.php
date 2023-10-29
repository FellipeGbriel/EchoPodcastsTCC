<?php
$ambiente = false;

if ($ambiente) { //Ambiente de Produção

    $HostName = "localhost";
    $HostUser = "id17479246_admin";
    $HostPass = "RS8u%qAUZf!6Knyc";
    $DatabaseName = "id17479246_testeechobd";
}

else { // Ambiente de Desenvolvimento

    $HostName = "localhost";
    $HostUser = "root";
    $HostPass = "";
    $DatabaseName = "echopod_bd";

}
?>