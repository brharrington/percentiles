set title 'All'
set term png
set output 'healthcheck-errors.png'
plot 'build/results/healthcheck.dat' using 1:4 title 'tdigest' with lines, \
     'build/results/healthcheck.dat' using 1:6 title 'spectator' with lines
