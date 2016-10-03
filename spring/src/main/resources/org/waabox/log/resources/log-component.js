(function (define) {
  'use strict';

  (function (root, factory) {
    if (typeof define === 'function' && define.amd) {
      define(factory);
    } else if (typeof exports === 'object') {
      module.exports = factory();
    } else {
      root.LogView = factory();
    }
  }(window, function () {
    return (function () {

      /** List of events supported by the log view. */
      var events = [
        'expand',
        'expandAll',
        'collapse',
        'collapseAll',
        'select'
      ];

      /**
       * A forEach that will work with a NodeList and generic Arrays
       * @param {array|NodeList} arr The array to iterate over
       * @param {function} callback Function that executes for each element.
       *  First parameter is element, second is index
       * @param {object} The context to execute callback with
       */
      function forEach(arr, callback, scope) {
        for (var i = 0; i < arr.length; i += 1) {
          callback.call(scope, arr[i], i);
        }
      }

      /**
       * Emit an event from the log view
       * @param {string} name The name of the event to emit
       */
      function emit(instance, name) {
        var args = [].slice.call(arguments, 2);
        if (events.indexOf(name) > -1) {
          if (instance.handlers[name] 
            && instance.handlers[name] instanceof Array) {
            forEach(instance.handlers[name], function (handle) {
              window.setTimeout(function () {
                handle.callback.apply(handle.context, args);
              }, 0);
            });
          }
        } else {
          throw new Error(name + ' event cannot be found on LogView.');
        }
      }

      /**
       * Renders the log view in the DOM
       */
      function render(self) {
        var container = document.getElementById(self.node);
        var leaves = [], click;

        var renderLeaf = function (item) {
          var leaf = document.createElement('div');
          var content = document.createElement('div');
          var text = document.createElement('div');
          var expando = document.createElement('div');

          var renderItem = function() {
            var out = item.stack;
            out = out.substring(out.lastIndexOf("."), out.length);
            out = out.substring(1);
            if (item.value) {
              out = item.value;
            }
            return out;
          }

          leaf.setAttribute('class', 'log-leaf');

          content.setAttribute('class', 'log-leaf-content');
          content.setAttribute('data-item', JSON.stringify(item));

          text.setAttribute('class', 'log-leaf-text');
          text.textContent = renderItem();

          expando.setAttribute('class', 'log-expand ' 
              + (item.expanded ? 'expanded' : ''));
          expando.textContent = item.expanded ? '-' : '+';

          content.appendChild(expando);
          content.appendChild(text);
          leaf.appendChild(content);

          if (item.nodes.length > 0) {
            var children = document.createElement('div');
            children.setAttribute('class', 'log-child-leaves');

            forEach(item.nodes, function (child) {
              var childLeaf = renderLeaf(child);
              children.appendChild(childLeaf);
            });

            if (!item.expanded) {
              children.classList.add('hidden');
            }

            leaf.appendChild(children);
          } else {
            expando.classList.add('hidden');
          }
          return leaf;
        };

        forEach(self.data, function (item) {
          leaves.push(renderLeaf.call(self, item));
        });

        container.innerHTML = leaves.map(function (leaf) {
          return leaf.outerHTML;
        }).join('');

        click = function (e) {
          var parent = (e.target || e.currentTarget).parentNode;
          var data = JSON.parse(parent.getAttribute('data-item'));
          var leaves = parent.parentNode.querySelector('.log-child-leaves');
          if (leaves) {
            if (leaves.classList.contains('hidden')) {
              self.expand(parent, leaves);
            } else {
              self.collapse(parent, leaves);
            }
          } else {
            emit(self, 'select', {
              target: e,
              data: data
            });
          }
        };

        forEach(container.querySelectorAll('.log-leaf-text'), function (node) {
          node.onclick = click;
        });

        forEach(container.querySelectorAll('.log-expand'), function (node) {
          node.onclick = click;
        });

      }

      /** @constructor
       *
       * Creates a new instance of the LogView.
       *
       * @property {object} handlers The attached event handlers.
       * @property {object} data The JSON object that represents 
       *  the tree structure.
       * @property {DOMElement} node The DOM element to render the tree in.
       * @property {object} from the company name.
       */
      function LogView(data, node, from) {
        this.handlers = {};

        /** The DOM element.*/
        this.node = node;

        /** Configures the LogView for a company.*/
        this.configure = function(node, company, logView) {
          node.nodes.find(function(n) {
            if (n.stack.indexOf(company) > -1) {
              logView.data = n.nodes;
              return true;
            }
            logView.configure(n, company, logView);
          });
        }

        if (from) {
          this.configure(data, from, this);
        } else {
          this.data = data.nodes;
        }

        if (this.data) {
          render(this);
        } else {
          document.getElementById(this.node).innerHTML = "No data to display";
        }

      }

      /**
       * Expands a leaflet by the expando or the leaf text
       * @param {DOMElement} node The parent node that contains the leaves
       * @param {DOMElement} leaves The leaves wrapper element
       */
      LogView.prototype.expand = function (node, leaves, skipEmit) {
        var expando = node.querySelector('.log-expand');
        expando.textContent = '-';
        leaves.classList.remove('hidden');
        if (skipEmit) { return; }
        emit(this, 'expand', {
          target: node,
          leaves: leaves
        });
      };

      LogView.prototype.expandAll = function () {
        var self = this;
        var nodes = document.getElementById(self.node)
          .querySelectorAll('.log-expand');
        forEach(nodes, function (node) {
          var parent = node.parentNode;
          var leaves = parent.parentNode.querySelector('.log-child-leaves');
          if (parent && leaves && parent.hasAttribute('data-item')) {
            self.expand(parent, leaves, true);
          }
        });
        emit(this, 'expandAll', {});
      };

      /** Collapses a leaflet by the expand or the leaf text.
       * @param {DOMElement} node The parent node that contains the leaves
       * @param {DOMElement} leaves The leaves wrapper element
       */
      LogView.prototype.collapse = function (node, leaves, skipEmit) {
        var expando = node.querySelector('.log-expand');
        expando.textContent = '+';
        leaves.classList.add('hidden');
        if (skipEmit) { return; }
        emit(this, 'collapse', {
          target: node,
          leaves: leaves
        });
      };

      /** Collapse all the elements.*/
      LogView.prototype.collapseAll = function () {
        var self = this;
        var nodes = document.getElementById(self.node)
          .querySelectorAll('.log-expand');
        forEach(nodes, function (node) {
          var parent = node.parentNode;
          var leaves = parent.parentNode.querySelector('.log-child-leaves');
          if (parent && leaves && parent.hasAttribute('data-item')) {
            self.collapse(parent, leaves, true);
          }
        });
        emit(this, 'collapseAll', {});
      };

      /** Attach an event handler to the log view.
       * @param {string} name Name of the event to attach
       * @param {function} callback The callback to execute on the event
       * @param {object} scope The context to call the callback with
       */
      LogView.prototype.on = function (name, callback, scope) {
        if (events.indexOf(name) > -1) {
          if (!this.handlers[name]) {
            this.handlers[name] = [];
          }
          this.handlers[name].push({
            callback: callback,
            context: scope
          });
        } else {
          throw new Error(name + ' is not supported by LogView.');
        }
      };

      /** Deattach an event handler from the log view
       * @param {string} name Name of the event to deattach.
       * @param {function} callback The function to deattach.
       */
      LogView.prototype.off = function (name, callback) {
        var index, found = false;
        if (this.handlers[name] instanceof Array) {
          this.handlers[name].forEach(function (handle, i) {
            index = i;
            if (handle.callback === callback && !found) {
              found = true;
            }
          });
          if (found) {
            this.handlers[name].splice(index, 1);
          }
        }
      };

      return LogView;
    }());
  }));
}(window.define));
