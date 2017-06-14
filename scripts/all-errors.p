set title 'All'
set term png
set output 'all-errors.png'
plot 'build/results/all.dat' using 1:4 title 'tdigest' with lines, \
     'build/results/all.dat' using 1:6 title 'spectator' with lines, \
     'build/results/all.dat' using 1:8 title 'powers of 2' with lines, \
     'build/results/all.dat' using 1:10 title 'powers of 4' with lines, \
     'build/results/all.dat' using 1:12 title 'local-avg' with lines
