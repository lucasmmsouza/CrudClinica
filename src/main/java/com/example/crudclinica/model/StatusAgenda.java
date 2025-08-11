package com.example.crudclinica.model;

public enum StatusAgenda {
    DISPONIVEL, // Aberto para agendamento
    AGENDADO,   // Já preenchido
    CANCELADO,  // O paciente cancelou, pode voltar a ficar disponível
    BLOQUEADO   // O médico bloqueou por um motivo pessoal
}