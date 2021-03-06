package com.doitteam.doit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doitteam.doit.domain.ParticipacionReto;
import com.doitteam.doit.domain.User;
import com.doitteam.doit.repository.ParticipacionRetoRepository;
import com.doitteam.doit.repository.UserRepository;
import com.doitteam.doit.security.SecurityUtils;
import com.doitteam.doit.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ParticipacionReto.
 */
@RestController
@RequestMapping("/api")
public class ParticipacionRetoResource {

    private final Logger log = LoggerFactory.getLogger(ParticipacionRetoResource.class);

    private static final String ENTITY_NAME = "participacionReto";

    private final ParticipacionRetoRepository participacionRetoRepository;
    private final UserRepository userRepository;

    public ParticipacionRetoResource(ParticipacionRetoRepository participacionRetoRepository,
                                     UserRepository userRepository){
        this.participacionRetoRepository = participacionRetoRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST  /participacion-retos : Create a new participacionReto.
     *
     * @param participacionReto the participacionReto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new participacionReto, or with status 400 (Bad Request) if the participacionReto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/participacion-retos")
    @Timed
    public ResponseEntity<ParticipacionReto> createParticipacionReto(@RequestBody ParticipacionReto participacionReto) throws URISyntaxException {
        log.debug("REST request to save ParticipacionReto : {}", participacionReto);
        if (participacionReto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new participacionReto cannot already have an ID")).body(null);
        }
        ZonedDateTime horaSistema = ZonedDateTime.now();
        participacionReto.setHoraPublicacion(horaSistema);

        User usuario = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        participacionReto.setUsuario(usuario);

        Integer existing = participacionRetoRepository.getExistingParticipacion(participacionReto.getUsuario().getId(), participacionReto.getReto().getId());
        if(existing > 1){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "Ya has participado", "A new participacionReto cannot already have an ID")).body(null);
        }

        ParticipacionReto result = participacionRetoRepository.save(participacionReto);
        return ResponseEntity.created(new URI("/api/participacion-retos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /participacion-retos : Updates an existing participacionReto.
     *
     * @param participacionReto the participacionReto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated participacionReto,
     * or with status 400 (Bad Request) if the participacionReto is not valid,
     * or with status 500 (Internal Server Error) if the participacionReto couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/participacion-retos")
    @Timed
    public ResponseEntity<ParticipacionReto> updateParticipacionReto(@RequestBody ParticipacionReto participacionReto) throws URISyntaxException {
        log.debug("REST request to update ParticipacionReto : {}", participacionReto);
        if (participacionReto.getId() == null) {
            return createParticipacionReto(participacionReto);
        }
        ParticipacionReto result = participacionRetoRepository.save(participacionReto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, participacionReto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /participacion-retos : get all the participacionRetos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of participacionRetos in body
     */
    @GetMapping("/participacion-retos")
    @Timed
    public List<ParticipacionReto> getAllParticipacionRetos() {
        log.debug("REST request to get all ParticipacionRetos");
        List<ParticipacionReto> participacionRetos = participacionRetoRepository.findAll();
        return participacionRetos;
    }

    /**
     * GET  /participacion-retos/:id : get the "id" participacionReto.
     *
     * @param id the id of the participacionReto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the participacionReto, or with status 404 (Not Found)
     */
    @GetMapping("/participacion-retos/{id}")
    @Timed
    public ResponseEntity<ParticipacionReto> getParticipacionReto(@PathVariable Long id) {
        log.debug("REST request to get ParticipacionReto : {}", id);
        ParticipacionReto participacionReto = participacionRetoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(participacionReto));
    }

    // GET DE LAS PARTICIPACIONES RETO DE TUS AMIGOS MENOS LAS TUYAS
    @GetMapping("/participacionesFriends")
    @Timed
    public List<ParticipacionReto> getParticipacionesFriends(){
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        return participacionRetoRepository.getParticipacionesFriends(user.getId());
    }

    // GET TUS PARTICIPACIONES
    @GetMapping("/misParticipaciones")
    @Timed
    public List<ParticipacionReto> getMisParticipaciones(){
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        return participacionRetoRepository.getMisParticipaciones(user.getId());
    }

    // GET LIKES POR PARTICIPACION
    @GetMapping("/likesParticipacion/{idParticipacion}")
    @Timed
    public Integer likesParticipacion(@PathVariable Long idParticipacion){
        return participacionRetoRepository.getLikesParticipacion(idParticipacion);
    }

    /**
     * DELETE  /participacion-retos/:id : delete the "id" participacionReto.
     *
     * @param id the id of the participacionReto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/participacion-retos/{id}")
    @Timed
    @Transactional
    public ResponseEntity<Void> deleteParticipacionReto(@PathVariable Long id) {
        log.debug("REST request to delete ParticipacionReto : {}", id);
        participacionRetoRepository.deleteLikesParticipacion(id);
        participacionRetoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
