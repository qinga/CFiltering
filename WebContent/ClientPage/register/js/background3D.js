(function(t, e) {
	function i(i, s) {
		function r() {
			this.x = Math.random() * i.width,
				this.y = Math.random() * i.height,
				this.vx = u.velocity - .5 * Math.random(),
				this.vy = u.velocity - .5 * Math.random(),
				this.radius = Math.random() * u.star.width
		}
		var n = t(i),
			a = i.getContext("2d"),
			o = {
				star: {
					color: "rgba(224, 224, 224, .7)",
					width: 1
				},
				line: {
					color: "rgba(224, 224, 224, .7)",
					width: .2
				},
				position: {
					x: 0,
					y: 0
				},
				width: e.innerWidth,
				height: e.innerHeight,
				velocity: .1,
				length: 100,
				distance: 100,
				radius: 150,
				stars: []
			},
			u = t.extend(!0, {}, o, s);
			r.prototype = {
				create: function() {
					a.beginPath(),
						a.arc(this.x, this.y, this.radius, 0, 2 * Math.PI, !1),
						a.fill()
				},
				animate: function() {
					var t;
					for(t = 0; t < u.length; t++) {
						var e = u.stars[t];
						e.y < 0 || e.y > i.height ? (e.vx = e.vx, e.vy = -e.vy) : (e.x < 0 || e.x > i.width) && (e.vx = -e.vx, e.vy = e.vy),
							e.x += e.vx,
							e.y += e.vy
					}
				},
				line: function() {
				var t, e, i, s, r = u.length;
				for(i = 0; r > i; i++)
					for(s = 0; r > s; s++)
						t = u.stars[i],
						e = u.stars[s],
						t.x - e.x < u.distance && t.y - e.y < u.distance && t.x - e.x > -u.distance && t.y - e.y > -u.distance && t.x - u.position.x < u.radius && t.y - u.position.y < u.radius && t.x - u.position.x > -u.radius && t.y - u.position.y > -u.radius && (
							a.beginPath(),
							a.moveTo(t.x, t.y),
							a.lineTo(e.x, e.y),
							a.stroke(),
							a.closePath()
						)
					}
				}, 
				this.createStars = function() {
					var t, e, s = u.length;
					for(a.clearRect(0, 0, i.width, i.height), e = 0; s > e; e++)
						u.stars.push(new r),
						t = u.stars[e],
						t.create();
					t.line(),
						t.animate()
				},
				this.setCanvas = function() {
					i.width = u.width,
						i.height = u.height
				},
				this.setContext = function() {
					a.fillStyle = u.star.color,
						a.strokeStyle = u.line.color,
						a.lineWidth = u.line.width
				}, this.setInitialPosition = function() {
					s && s.hasOwnProperty("position") || (u.position = {
						x: .5 * i.width,
						y: .5 * i.height
					})
				},
				this.loop = function(t) {
					t(), e.requestAnimationFrame(function() {
						this.loop(t)
					}.bind(this))
				}, 
				this.bind = function() {
					t(document).on("mousemove", function(t) {
						u.position.x = t.pageX - n.offset().left,
						u.position.y = t.pageY - n.offset().top
					})
				},
				this.init = function() {
			this.setCanvas(),
				this.setContext(),
				this.setInitialPosition(),
				this.loop(this.createStars),
				this.bind()
		}
			}
			t.fn.constellation = function(t) {
				return this.each(function() {
					new i(this, t).init()
			})
		}
	})($, window),

$(function() {
	$("#constellation").constellation({
		distance: 120,
		star: {
			width: 1
		},
		line: {
			width: .2
		}
	})
});