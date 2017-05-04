package com.doitteam.doit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doitteam.doit.domain.Evento;
import com.doitteam.doit.domain.InvitacionEvento;
import com.doitteam.doit.domain.User;
import com.doitteam.doit.repository.EventoRepository;
import com.doitteam.doit.repository.InvitacionEventoRepository;
import com.doitteam.doit.repository.UserRepository;
import com.doitteam.doit.security.SecurityUtils;
import com.doitteam.doit.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing InvitacionEvento.
 */
@RestController
@RequestMapping("/api")
public class InvitacionEventoResource {

    private final Logger log = LoggerFactory.getLogger(InvitacionEventoResource.class);

    private static final String ENTITY_NAME = "invitacionEvento";

    private final InvitacionEventoRepository invitacionEventoRepository;
    private final UserRepository userRepository;
    private final EventoRepository eventoRepository;

    public InvitacionEventoResource(InvitacionEventoRepository invitacionEventoRepository, UserRepository userRepository, EventoRepository eventoRepository) {
        this.invitacionEventoRepository = invitacionEventoRepository;
        this.userRepository = userRepository;
        this.eventoRepository = eventoRepository;
    }

    /**
     * POST  /invitacion-eventos : Create a new invitacionEvento.
     *
     * @param invitacionEvento the invitacionEvento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invitacionEvento, or with status 400 (Bad Request) if the invitacionEvento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invitacion-eventos")
    @Timed
    public ResponseEntity<InvitacionEvento> createInvitacionEvento(@Valid @RequestBody InvitacionEvento invitacionEvento) throws URISyntaxException {
        log.debug("REST request to save InvitacionEvento : {}", invitacionEvento);
        if (invitacionEvento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new invitacionEvento cannot already have an ID")).body(null);
        }
        InvitacionEvento result = invitacionEventoRepository.save(invitacionEvento);
        return ResponseEntity.created(new URI("/api/invitacion-eventos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invitacion-eventos : Updates an existing invitacionEvento.
     *
     * @param invitacionEvento the invitacionEvento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invitacionEvento,
     * or with status 400 (Bad Request) if the invitacionEvento is not valid,
     * or with status 500 (Internal Server Error) if the invitacionEvento couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invitacion-eventos")
    @Timed
    public ResponseEntity<InvitacionEvento> updateInvitacionEvento(@Valid @RequestBody InvitacionEvento invitacionEvento) throws URISyntaxException {
        log.debug("REST request to update InvitacionEvento : {}", invitacionEvento);
        if (invitacionEvento.getId() == null) {
            return createInvitacionEvento(invitacionEvento);
        }
        InvitacionEvento result = invitacionEventoRepository.save(invitacionEvento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invitacionEvento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invitacion-eventos : get all the invitacionEventos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of invitacionEventos in body
     */
    @GetMapping("/invitacion-eventos")
    @Timed
    public List<InvitacionEvento> getAllInvitacionEventos() {
        log.debug("REST request to get all InvitacionEventos");
        List<InvitacionEvento> invitacionEventos = invitacionEventoRepository.findAll();
        return invitacionEventos;
    }

    /**
     * GET  /invitacion-eventos/:id : get the "id" invitacionEvento.
     *
     * @param id the id of the invitacionEvento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invitacionEvento, or with status 404 (Not Found)
     */
    @GetMapping("/invitacion-eventos/{id}")
    @Timed
    public ResponseEntity<InvitacionEvento> getInvitacionEvento(@PathVariable Long id) {
        log.debug("REST request to get InvitacionEvento : {}", id);
        InvitacionEvento invitacionEvento = invitacionEventoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(invitacionEvento));
    }

    /**
     * DELETE  /invitacion-eventos/:id : delete the "id" invitacionEvento.
     *
     * @param id the id of the invitacionEvento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invitacion-eventos/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvitacionEvento(@PathVariable Long id) {
        log.debug("REST request to delete InvitacionEvento : {}", id);
        invitacionEventoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    //para que el usuario logeado se apunte a un evento de la lista
    @PostMapping("/invitacion-eventos/{id}/apuntarse")
    @Timed
    public ResponseEntity<InvitacionEvento> apuntarse(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to apuntarte : {}");
        //tenemos el usuario logeado
        User userLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        //tenemos el evento clicado por el usuario logeado
        Evento evento = eventoRepository.findOne(id);
        //tenemos la hora de ahora (apuntarse) al evento
        ZonedDateTime ahora = ZonedDateTime.now();

        //control de error
        //comprobar que la id_evento no sea null
        //comprobar que la invitacion del evento no sea null

        InvitacionEvento invitacion = new InvitacionEvento();
        invitacion.setHoraInvitacion(ahora);
        invitacion.setMiembroEvento(userLogin);
        invitacion.setEvento(evento);
        invitacion.setInvitado(userLogin);
       /* if (invitacion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new invitacionEvento cannot already have an ID")).body(null);
        */
        InvitacionEvento result = invitacionEventoRepository.save(invitacion);
        return ResponseEntity.created(new URI("/api/invitacion-eventos/apuntarse" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

}
