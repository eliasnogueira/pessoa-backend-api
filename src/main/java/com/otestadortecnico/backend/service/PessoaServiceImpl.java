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
package com.otestadortecnico.backend.service;

import com.otestadortecnico.backend.entity.Pessoa;
import com.otestadortecnico.backend.exception.PessoaNotFoundException;
import com.otestadortecnico.backend.repositories.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service("pessoaService")
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository repository;

    public PessoaServiceImpl(PessoaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Pessoa> get() {
        return repository.findAll();
    }

    @Override
    public Optional<Pessoa> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public Pessoa save(Pessoa pessoa) {
        return repository.save(pessoa);
    }

    @Override
    public Optional<Pessoa> update(Pessoa novaPessoa, Long id) {
        return repository.findById(id).
            map(pessoa -> {
                setIfNotNull(pessoa::setNome, novaPessoa.getNome());
                setIfNotNull(pessoa::setEndereco, novaPessoa.getEndereco());
                setIfNotNull(pessoa::setHobbies, novaPessoa.getHobbies());

                return repository.save(pessoa);
            });
    }

    @Override
    public void deleteByID(Long id) {
        Optional<Pessoa> pessoa = get(id);

        if(!pessoa.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new PessoaNotFoundException(id);
        }
    }

    private <T> void setIfNotNull(final Consumer<T> setter, final T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
