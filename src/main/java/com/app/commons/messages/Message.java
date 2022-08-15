package com.app.commons.messages;

public interface Message {

    String ERROR_MESSAGE_PERIODO_INVALID = "Periodo informado invalido !!! São permitidos apenas: diario, semanal, mensal";

    String ERROR_MESSAGE_FILE_UPLOAD_PERIODO = "Não é possível realizar upload do arquivo, pois o periodo selecionado e o arquivo nao coincidem !!! ";

    String ERROR_MESSAGE_FILE_UPLOAD_EMPTY = "Não é possível realizar upload sem um arquivo informado";

    String ERROR_MESSAGE_ANO_MES_INVALID = "Ano e mês informados são inválido. Formato válido: YYYY/MM";
}
