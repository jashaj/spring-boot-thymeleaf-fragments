/*
 *   Copyright 2016 Jasha Joachimsthal
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

(function () {
    function initInlineEditable() {
        $.fn.editable.defaults.mode = 'inline';
        $('.inline-editable').editable();
    }

    function initDraggableModals() {
        $('.modal-dialog').draggable({
            handle: ".modal-header"
        });
    }

    function openEditFormInModal() {
        // change all city-link links into modal triggers, but don't let Bootstrap do its magic loading the content into the
        // modal-content div because it messes up the UI.
        $('.city-link')
            .attr('role', 'button')
            .attr('data-toggle', 'modal')
            .attr('data-target', '#cityModal')
            .attr('data-remote', 'false')
            .addClass('btn btn-default');

        // loads content into .modal-body when the modal is shown
        var $cityModal = $('#cityModal');
        $cityModal.on('show.bs.modal', function (e) {
            var $modal = $(this);
            var trigger = e.relatedTarget;
            var location = trigger.getAttribute("href");
            $modal.find('.modal-body').load(location, initInlineEditable);
        });

        // Closes the modal when the user clicks the cancel button instead of following its link
        $cityModal.on('click', '.btn-cancel', function (e) {
            e.preventDefault();
            $cityModal.modal('hide');
        });
    }

    function addButtonToRemoveEnancedUx() {
        // For demo: add a button to remove the modal magic
        var $removeMagicBtn = $('<button/>')
            .addClass('btn btn-default')
            .attr('type', 'button')
            .text('Remove all magic')
            .on('click', function () {
                $(this).remove();
                $('.inline-editable').editable('destroy');
                $('.city-link')
                    .removeAttr('role')
                    .removeAttr('data-toggle')
                    .removeAttr('data-target')
                    .removeAttr('data-remote')
                    .removeClass('btn btn-default');
            });
        $('#cities').after($removeMagicBtn);
    }


    openEditFormInModal();
    initDraggableModals();
    initInlineEditable();
    addButtonToRemoveEnancedUx();
})();