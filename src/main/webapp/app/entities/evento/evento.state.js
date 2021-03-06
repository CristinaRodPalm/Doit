(function () {
    'use strict';

    angular
        .module('doitApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('evento', {
                parent: 'entity',
                url: '/evento',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'doitApp.evento.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/evento/eventos.html',
                        controller: 'EventoController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('evento');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('eventos', {
                parent: 'evento',
                url: '/lista-eventos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'doitApp.evento.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/evento/lista-eventos.html',
                        controller: 'EventoController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('evento');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('evento-detail', {
                parent: 'evento',
                url: '/evento/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'doitApp.evento.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/evento/evento-detail.html',
                        controller: 'EventoDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('evento');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Evento', function ($stateParams, Evento) {
                        return Evento.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'eventos',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('evento-detail.edit', {
                parent: 'evento-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/evento/evento-edit.html',
                        controller: 'EventoEditController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Evento', function (Evento) {
                                return Evento.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('evento.new', {
                parent: 'evento',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/evento/evento-dialog.html',
                        controller: 'EventoDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            nombre: null,
                            descripcion: null,
                            hora: null,
                            imagen: null,
                            imagenContentType: null,
                            direccion: null,
                            latitud: null,
                            longitud: null,
                            fechaEvento: null,
                            id: null
                        };
                    }
                },
            })
            .state('evento.edit', {
                parent: 'eventos',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/evento/evento-edit.html',
                        controller: 'EventoEditController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Evento', function (Evento) {
                                return Evento.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('eventos', null, {reload: 'eventos'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('evento.delete', {
                parent: 'evento',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/evento/evento-delete-dialog.html',
                        controller: 'EventoDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Evento', function (Evento) {
                                return Evento.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('eventos', null, {reload: 'eventos'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('evento-search', {
                parent: 'evento',
                url: '/buscar-eventos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Buscar eventos'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/evento/evento-search.html',
                        controller: 'EventoController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('evento');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('eventos-mundo', {
                parent: 'evento',
                url: '/eventos-mundo',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'doitApp.evento.home.createLabel'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/evento/eventos-mundo.html',
                        controller: 'mapController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('evento');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
    }

})();
