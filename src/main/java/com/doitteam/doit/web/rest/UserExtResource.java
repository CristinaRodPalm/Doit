package com.doitteam.doit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.doitteam.doit.domain.User;
import com.doitteam.doit.domain.UserExt;
import com.doitteam.doit.repository.UserExtRepository;
import com.doitteam.doit.repository.UserRepository;
import com.doitteam.doit.security.SecurityUtils;
import com.doitteam.doit.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserExt.
 */
@RestController
@RequestMapping("/api")
public class UserExtResource {

    private final Logger log = LoggerFactory.getLogger(UserExtResource.class);

    private static final String ENTITY_NAME = "userExt";

    private final UserExtRepository userExtRepository;

    private final UserRepository userRepository;

    public UserExtResource(UserExtRepository userExtRepository, UserRepository userRepository) {
        this.userExtRepository = userExtRepository;
        this.userRepository = userRepository;
    }

    /**
     * POST  /user-exts : Create a new userExt.
     *
     * @param userExt the userExt to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userExt, or with status 400 (Bad Request) if the userExt has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-exts")
    @Timed
    public ResponseEntity<UserExt> createUserExt(@Valid @RequestBody UserExt userExt) throws URISyntaxException {
        log.debug("REST request to save UserExt : {}", userExt);
        if (userExt.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userExt cannot already have an ID")).body(null);
        }
        UserExt result = userExtRepository.save(userExt);
        return ResponseEntity.created(new URI("/api/user-exts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-exts : Updates an existing userExt.
     *
     * @param userExt the userExt to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userExt,
     * or with status 400 (Bad Request) if the userExt is not valid,
     * or with status 500 (Internal Server Error) if the userExt couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-exts")
    @Timed
    public ResponseEntity<UserExt> updateUserExt(@Valid @RequestBody UserExt userExt) throws URISyntaxException {
        log.debug("REST request to update UserExt : {}", userExt);
        if (userExt.getId() == null) {
            return createUserExt(userExt);
        }
        UserExt result = userExtRepository.save(userExt);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userExt.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-exts : get all the userExts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userExts in body
     */
    @GetMapping("/user-exts")
    @Timed
    public List<UserExt> getAllUserExts() {
        log.debug("REST request to get all UserExts");
        List<UserExt> userExts = userExtRepository.findAll();
        return userExts;
    }

    @GetMapping("/user-exts/{id}")
    @Timed
    public ResponseEntity<UserExt> getUserExt(@PathVariable Long id) {
        log.debug("REST request to get UserExt : {}", id);
        UserExt userExt = userExtRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userExt));
    }

    @DeleteMapping("/user-exts/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserExt(@PathVariable Long id) {
        log.debug("REST request to delete UserExt : {}", id);
        userExtRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/userExtByUser")
    @Timed
    public UserExt getUserExtByUser (){
        User userLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        return userExtRepository.findByUserID(userLogin.getId());
    }

    @GetMapping("/userExtByUserID/{id}")
    @Timed
    public UserExt getUserExtByUserID (@PathVariable Long id){
        UserExt userExt = userExtRepository.findByUserID(userRepository.findOne(id).getId());
        return userExtRepository.findByUserID(userExt.getId());

    }

}

