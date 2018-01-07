/*
 *   Copyright 2018 Jasha Joachimsthal
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

(function ($) {
  function initModalEventHandlers() {
    // loads content into .modal-body when the modal is being shown
    var $cityModal = $('#cityModal');
    $cityModal.on('show.bs.modal', function (e) {
      var $modal = $(e.target);
      var trigger = e.relatedTarget;
      var location = trigger.getAttribute('href');
      $modal.find('.modal-title').text(trigger.innerText);
      $modal.find('.modal-body').load(location);
    });

    // Closes the modal when the user clicks the cancel button instead of following its link
    $('.modal').on('click', '.btn-cancel', function (e) {
      e.preventDefault();
      $(e.delegateTarget).modal('hide');
    });

    // Make modals draggable
    $('.modal-dialog').draggable({
      handle: '.modal-header'
    });
  }

  initModalEventHandlers();
})(jQuery);