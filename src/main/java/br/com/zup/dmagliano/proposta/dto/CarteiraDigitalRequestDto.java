package br.com.zup.dmagliano.proposta.dto;

import br.com.zup.dmagliano.proposta.enums.CarteiraDigitalEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CarteiraDigitalRequestDto {

    @Email
    private String email;
    @NotNull
    private CarteiraDigitalEnum carteira;

    public String getEmail() {
        return email;
    }

    public CarteiraDigitalEnum getCarteira() {
        return carteira;
    }
}
