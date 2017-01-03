var width = 1024, height = 600;

var svg = d3.select("svg")
	.attr("width", width)
	.attr("height", height);

var simulation = d3.forceSimulation()
	.force("link", d3.forceLink()
			.id(function(d) { return d.id; })
			.distance(150)
			.strength(1))
	.force("collide",d3.forceCollide(75))
	.force("center", d3.forceCenter(width / 2.4, height / 2.4));

var jsonGraph = $("#graph_json").val();

var nodeLabel = document.querySelector('.node_label');

var graph = JSON.parse(jsonGraph);

simulation
	.nodes(graph.nodes)
	.on("tick", tick);

simulation.force("link")
	.links(graph.links);
  
svg.append("defs").selectAll("marker")
	.data(
			["CATALYSE",
			 "HAS",
			 "MATCHES",
			 "SUBSTRATE_FOR",
			 "PRODUCTOF"])
	.enter()
	.append("marker")
	.attr("id", function(d) {return d;})
	.attr("viewBox", "0 -5 10 10")
	.attr("refX", 15)
	.attr("refY", -1.5)
	.attr( "markerWidth", 15)
	.attr("markerHeight", 15)
	.attr("orient", "auto")
	.append("path")
	.attr("d", "M0,-2L5,0L0,02");

var path = svg.append("g")
	.selectAll("path")
	.data(graph.links)
	.enter()
	.append("path")
	.attr("class", function(d) {
		return "link " + d.type;
	})
	.attr("marker-end", function(d) {
		return "url(#" + d.type + ")";});
  
var circle = svg.append("g")
	.selectAll("circle")
	.data(graph.nodes)
	.enter()
	.append("circle")
	.attr("r", 22)
	.attr("class", function(d) {return "node" + d.label;})
	.attr("onmouseover", function(d) {
		return "showLabels(this, \""+ d.label+"\",\""+d.name+"\",\""+d.propertie+"\");"})
	.attr("onmouseout", function(d) {
		return "clearLabels(this, \""+ d.color+"\")"})
	.call(d3.drag()
			.on("start", dragstarted)
			.on("drag", dragged)
			.on("end", dragended));

function showLabels(e, label, name, propertie) {
	// Mostra propriedades em um campo da página
	e.style.fill = "#55a";	
	nodeLabel.innerHTML = 
		"<h3><b>" + label.substring(0, label.length - 1) +
		"</b></h3><h4><b>Name</b>: " + name +
		"<br/><b>ID: </b>: " + propertie + "</h4>";
}

function clearLabels(e, color) {
	e.style.fill = color;
}

circle.append("title")
.text(function(d) { return d.propertie; });

var text = svg.append("g").selectAll("text")
	.data(graph.nodes)
	.enter()
	.append("text")
	.attr("x", -20)
	.attr("y", ".31em")
	.text(function(d) {return d.propertie;});
  
function tick() {
	path.attr("d", linkArc);
	circle.attr("transform", transform);
	text.attr("transform", transform);
}
  
function linkArc(d) {
	var dx = d.target.x - d.source.x,
	    dy = d.target.y - d.source.y,
	    dr = Math.sqrt(dx * dx + dy * dy);
	return "M" + d.source.x + "," + d.source.y + 
		   "A" + dr + "," + dr + " 0 0,1 " + 
		   d.target.x + "," + d.target.y;
}
  
function transform(d) {
	return "translate(" + d.x + "," + d.y + ")";
}
  
function dragstarted(d) {
	if (!d3.event.active) simulation.alphaTarget(0.3).restart();
	d.fx = d.x;
	d.fy = d.y;
}

function dragged(d) {
	d.fx = d3.event.x;
	d.fy = d3.event.y;
}

function dragended(d) {
	if (!d3.event.active) simulation.alphaTarget(0);
	d.fx = null;
	d.fy = null;
}
  