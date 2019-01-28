/*
 * MIT License
 *
 * Copyright (c) 2019 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.otestadortecnico.backend.controller;

import com.otestadortecnico.backend.domain.MessageDTO;
import com.otestadortecnico.backend.service.PessoaService;
import com.otestadortecnico.backend.entity.Pessoa;
import com.otestadortecnico.backend.domain.PessoaDTO;
import com.otestadortecnico.backend.exception.PessoaNotFoundException;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @ApiOperation(value = "Lista todas as pessoas cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pessoas encontradas")
    })
    @GetMapping("/api/v1/pessoas")
    List<Pessoa> getPessoa() {
        return pessoaService.get();
    }

    @ApiOperation(value = "Retorna uma pessoa pelo seu id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pessoa encontrada", response = PessoaDTO.class),
            @ApiResponse(code = 404, message = "Pessoa não encontrada", response = MessageDTO.class)
        })
    @GetMapping("/api/v1/pessoas/{id}")
    ResponseEntity<Pessoa> one(@PathVariable Long id) {
        return pessoaService.get(id)
                .map(pessoa -> ResponseEntity.ok().body(pessoa))
                .orElseThrow(() -> new PessoaNotFoundException(id));
    }

    @ApiOperation(value = "Adiciona uma pessoa")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pessoa criada com sucesso", response = PessoaDTO.class),
            @ApiResponse(code = 404, message = "Pessoa não encontrada", response = MessageDTO.class)
        })
    @PostMapping("/api/v1/pessoas")
    Pessoa novaPessoa(@Valid @RequestBody PessoaDTO pessoa) {
        return pessoaService.save(new ModelMapper().map(pessoa, Pessoa.class));
    }

    @ApiOperation(value = "Altera uma pessoa")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pessoa criada com sucesso", response = PessoaDTO.class),
            @ApiResponse(code = 404, message = "Pessoa não encontrada", response = MessageDTO.class)
        })
    @PutMapping("/api/v1/pessoas/{id}")
    Pessoa updatePessoa(@RequestBody PessoaDTO pessoaAlterada, @PathVariable Long id) {
        return pessoaService.update(new ModelMapper()
                .map(pessoaAlterada, Pessoa.class), id)
                .orElseThrow(() -> { throw new PessoaNotFoundException(id); });
    }

    @ApiOperation(value = "Remove uma pessoa pelo seu id")
    @ApiResponses( {
            @ApiResponse(code = 204, message = "Pessoa removida com sucesso"),
            @ApiResponse(code = 404, message = "Pessoa não encontrada", response = MessageDTO.class)
        })
    @DeleteMapping("/api/v1/pessoas/{id}")
    void delete(@PathVariable Long id) {
        pessoaService.deleteByID(id);
    }
}
