(function() {
    'use strict';

    angular
        .module('doitApp')
        .controller('EventoEditController', EventoEditController);

    EventoEditController.$inject = ['$uibModalInstance', 'NgMap', '$timeout', '$scope', '$state', 'DataUtils', 'entity', 'Evento', 'Reto', 'User', 'InvitacionEvento', 'Amistad'];

    function EventoEditController($uibModalInstance, NgMap, $timeout, $scope, $state, DataUtils, entity, Evento, Reto, User, InvitacionEvento, Amistad) {
        var vm = this;

        vm.evento = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.retos = Reto.query();
        vm.users = User.query();
        vm.invitacioneventos = InvitacionEvento.query();
        vm.friends = [];
        vm.inviteFriends = [];

        loadFriends();

        vm.placeChanged = function() {
            vm.place = this.getPlace();
            vm.map.setCenter(vm.place.geometry.location);
            vm.evento.latitud = vm.place.geometry.location.lat();
            vm.evento.longitud = vm.place.geometry.location.lng();
            vm.map.setZoom(15);
        }
        NgMap.getMap().then(function(map) {
            vm.map = map;
            vm.map.setZoom(6);
            vm.map.setCenter(new google.maps.LatLng(40.4378698, -3.8196217));
        });

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.evento.id !== null) {
                Evento.update(vm.evento, onSaveSuccess, onSaveError);
            } else {
                Evento.save(vm.evento, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('doitApp:eventoUpdate', result);
            $state.go('eventos', null, {reload: 'eventos'});
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.hora = false;

        vm.setImagen = function ($file, evento) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        evento.imagen = base64Data;
                        evento.imagenContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.fechaEvento = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function loadFriends(){
            Amistad.getFriends(function (result) {
                vm.friends = result;
            })
        }
    }
})();
