package com.rpmarketplace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/legal")
public class LegalController {
    @GetMapping("/terms")
    public LegalPage terms() {
        return new LegalPage("Termos de Uso", "Termos de uso para a plataforma de vendas RP.");
    }

    @GetMapping("/privacy")
    public LegalPage privacy() {
        return new LegalPage("Política de Privacidade", "Detalhes sobre coleta e uso de dados.");
    }

    @GetMapping("/purchase-rules")
    public LegalPage purchaseRules() {
        return new LegalPage("Regras de Compra", "Condições para compras e entrega digital.");
    }

    @GetMapping("/refund-policy")
    public LegalPage refundPolicy() {
        return new LegalPage("Política de Reembolso", "Regras para reembolsos e estornos.");
    }

    @GetMapping("/responsibility")
    public LegalPage responsibility() {
        return new LegalPage("Aviso de Responsabilidade", "Uso dos produtos é responsabilidade do comprador.");
    }

    public record LegalPage(String title, String body) {
    }
}
