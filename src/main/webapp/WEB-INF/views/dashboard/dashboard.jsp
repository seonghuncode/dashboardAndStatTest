<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- ECharts 라이브러리 CDN -->
<script
	src="https://cdn.jsdelivr.net/npm/echarts@5.4.3/dist/echarts.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- 차트 영역 스타일 -->
<style>
/*     #myChart {
      width: 600px;
      height: 400px;
      margin: 0 auto;
    } */
.dashboard {
	display: flex;
	gap: 20px;
	flex-wrap: wrap;
	padding: 20px;
}

.widget {
	width: 500px;
	border: 1px solid #ccc;
	border-radius: 12px;
	box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
	padding: 16px;
}

.widget-title {
	font-size: 16px;
	font-weight: bold;
	margin-bottom: 10px;
	text-align: center;
}

.chart-area {
	width: 100%;
	height: 377px;
}

/* 테이블 */
  .stat-table {
    width: 100%;
    max-width: 600px;
    margin: 20px auto;
    border-collapse: collapse;
    font-family: Arial, sans-serif;
    font-size: 14px;
  }

  .stat-table th, .stat-table td {
    border: 1px solid #ccc;
    padding: 10px;
    text-align: center;
  }

  .stat-table th {
    background-color: #f4f4f4;
    font-weight: bold;
  }

  .stat-table caption {
    caption-side: top;
    font-size: 16px;
    margin-bottom: 8px;
    font-weight: bold;
  }
  
</style>
<title>Insert title here</title>

</head>

<body>
	<script type="text/javascript">
		$(document).ready(function() {

			pieChart();

			lineChart();
			
			barChart();

		})

		// Pie차트---------------------------------------------------------------------------------------
		function pieChart() {
			var chartDom = document.getElementById('myChart');
			var myChart = echarts.init(chartDom);
			var option;

			option = {
				tooltip : {
					trigger : 'item'
				},
				legend : {
					top : '5%',
					left : 'center'
				},
				series : [ {
					name : 'Access From',
					type : 'pie',
					radius : [ '40%', '70%' ],
					avoidLabelOverlap : false,
					itemStyle : {
						borderRadius : 10,
						borderColor : '#fff',
						borderWidth : 2
					},
					label : {
						show : false,
						position : 'center'
					},
					emphasis : {
						label : {
							show : true,
							fontSize : 40,
							fontWeight : 'bold'
						}
					},
					labelLine : {
						show : false
					},
					data : [ {
						value : 1048,
						name : 'Search Engine'
					}, {
						value : 735,
						name : 'Direct'
					}, {
						value : 580,
						name : 'Email'
					}, {
						value : 484,
						name : 'Union Ads'
					}, {
						value : 300,
						name : 'Video Ads'
					} ]
				} ]
			};

			option && myChart.setOption(option);
		}

		//Line차트----------------------------------------------------------
		function lineChart() {
			var chartDom = document.getElementById('myLineChart');
			var myChart = echarts.init(chartDom);

			var option = {
				title : {
					text : 'Log Axis',
					left : 'center'
				},
				tooltip : {
					trigger : 'item',
					formatter : '{a} <br/>{b} : {c}'
				},
				legend : {
					left : 'left'
				},
				xAxis : {
					type : 'category',
					name : 'x',
					splitLine : {
						show : false
					},
					data : [ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' ]
				},
				grid : {
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true
				},
				yAxis : {
					type : 'log',
					name : 'y',
					minorSplitLine : {
						show : true
					}
				},
				series : [
						{
							name : 'Log2',
							type : 'line',
							data : [ 1, 3, 9, 27, 81, 247, 741, 2223, 6669 ]
						},
						{
							name : 'Log3',
							type : 'line',
							data : [ 1, 2, 4, 8, 16, 32, 64, 128, 256 ]
						},
						{
							name : 'Log1/2',
							type : 'line',
							data : [ 1 / 2, 1 / 4, 1 / 8, 1 / 16, 1 / 32,
									1 / 64, 1 / 128, 1 / 256, 1 / 512 ]
						} ]
			};

			myChart.setOption(option);
		}

		//Bar차트
		function barChart() {
			
			  const chartDom = document.getElementById("myBarChart");
			  const myChart = echarts.init(chartDom);

			  const rawData = [
			    [100, 302, 301, 334, 390, 330, 320],
			    [320, 132, 101, 134, 90, 230, 210],
			    [220, 182, 191, 234, 290, 330, 310],
			    [150, 212, 201, 154, 190, 330, 410],
			    [820, 832, 901, 934, 1290, 1330, 1320]
			  ];

			  const totalData = [];
			  for (let i = 0; i < rawData[0].length; ++i) {
			    let sum = 0;
			    for (let j = 0; j < rawData.length; ++j) {
			      sum += rawData[j][i];
			    }
			    totalData.push(sum);
			  }

			  const grid = {
			    left: 100,
			    right: 100,
			    top: 50,
			    bottom: 50
			  };

			  const gridWidth = myChart.getWidth() - grid.left - grid.right;
			  const gridHeight = myChart.getHeight() - grid.top - grid.bottom;
			  const categoryWidth = gridWidth / rawData[0].length;
			  const barWidth = categoryWidth * 0.6;
			  const barPadding = (categoryWidth - barWidth) / 2;

			  const color = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de'];

			  const series = [
			    'Direct',
			    'Mail Ad',
			    'Affiliate Ad',
			    'Video Ad',
			    'Search Engine'
			  ].map((name, sid) => {
			    return {
			      name,
			      type: 'bar',
			      stack: 'total',
			      barWidth: '60%',
			      label: {
			        show: true,
			        formatter: (params) => Math.round(params.value * 1000) / 10 + '%'
			      },
			      data: rawData[sid].map((d, did) =>
			        totalData[did] <= 0 ? 0 : d / totalData[did]
			      )
			    };
			  });

			  const elements = [];
			  for (let j = 1, jlen = rawData[0].length; j < jlen; ++j) {
			    const leftX = grid.left + categoryWidth * j - barPadding;
			    const rightX = leftX + barPadding * 2;
			    let leftY = grid.top + gridHeight;
			    let rightY = leftY;
			    for (let i = 0, len = series.length; i < len; ++i) {
			      const points = [];
			      const leftBarHeight = (rawData[i][j - 1] / totalData[j - 1]) * gridHeight;
			      points.push([leftX, leftY]);
			      points.push([leftX, leftY - leftBarHeight]);
			      const rightBarHeight = (rawData[i][j] / totalData[j]) * gridHeight;
			      points.push([rightX, rightY - rightBarHeight]);
			      points.push([rightX, rightY]);
			      points.push([leftX, leftY]);
			      leftY -= leftBarHeight;
			      rightY -= rightBarHeight;
			      elements.push({
			        type: 'polygon',
			        shape: {
			          points
			        },
			        style: {
			          fill: color[i],
			          opacity: 0.25
			        }
			      });
			    }
			  }

			  const option = {
			    legend: {
			      selectedMode: false
			    },
			    grid,
			    yAxis: {
			      type: 'value'
			    },
			    xAxis: {
			      type: 'category',
			      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
			    },
			    series,
			    graphic: {
			      elements
			    }
			  };

			  myChart.setOption(option);

			  // 반응형 처리
			  window.addEventListener('resize', () => {
			    myChart.resize();
			  });

		}
	</script>


	<h2 style="text-align: center;">대시보드</h2>

	<div style="display: flex;">

		<div class="dashboard">
			<!-- 위젯 1 -->
			<div class="widget">
				<div class="widget-title">접속 경로 통계</div>
				<div id="myChart" class="chart-area"></div>
			</div>
		</div>

		<div class="dashboard">
			<!-- 위젯 1 -->
			<div class="widget">
				<div class="widget-title">접속 경로 통계</div>
				<div id="myLineChart" class="chart-area"></div>
			</div>
		</div>

	</div>


	<div style="display: flex;">
	
		<div class="dashboard">
			<!-- 위젯 1 -->
			<div class="widget">
				<div class="widget-title">접속 경로 통계</div>
				<div id="myBarChart" class="chart-area"></div>
			</div>
		</div>
		
		<div class="dashboard">
			<div class="widget">
				<table class="stat-table">
				  <caption>일간 통계 요약</caption>
				  <thead>
				    <tr>
				      <th>날짜</th>
				      <th>방문자 수</th>
				      <th>페이지뷰</th>
				      <th>클릭률(%)</th>
				    </tr>
				  </thead>
				  <tbody>
				    <tr>
				      <td>2025-06-24</td>
				      <td>1,250</td>
				      <td>3,800</td>
				      <td>12.5%</td>
				    </tr>
				    <tr>
				      <td>2025-06-25</td>
				      <td>1,430</td>
				      <td>4,100</td>
				      <td>13.2%</td>
				    </tr>
				    <tr>
				      <td>2025-06-26</td>
				      <td>1,120</td>
				      <td>3,200</td>
				      <td>11.8%</td>
				    </tr>
				  </tbody>
				</table>
			</div>
		</div>
		
	</div>



</body>
</html>