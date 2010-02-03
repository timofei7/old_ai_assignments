'''
Created on Feb 1, 2010

@author: tim

X always goes first
'''
import re

class State(object):
    """This is a Game State"""

    def __init__(self):
        """Constructor"""
        self._state = ["......."] * 6; # number of rows
        self._moves = 0 #number of moves so far in game

        
    def encode(self):
        """returns a string representation of the _state"""
        s = "-1234567-\n"
        for row in self._state:
            s = s + "|" + row + "|\n"
        s = s + "-1234567-"
        return s
            
    def decode(self, str):
        """takes a string input and constructs this object with it"""
        i = 0
        for s in str.split("\n"):
            if re.match("^[|].*[|]$", s):
                self._state[i] = s.split("|")[1]
                i = i+1
                
        self._moves = self._count_turns() #set the next turn correctly
                        
    def legal_moves(self):
        """returns a string of legal moves there are from this _state"""
        return " ".join([str(int) for int in self._legal_moves()])
    
    
    def _legal_moves(self):
        """returns a list of legal moves there are from this _state"""
        l = []
        for index, column in enumerate(self._state[0]): #grab the topmost column
            if column == ".":
                l.append(index + 1) #use human column count
        return l
             
                
    def do_move(self, m):
        """does a move"""
        m = m - 1 #make start from 0
        rowcount = 0 #start from top but count as if from bottom
        if int(m + 1) in self._legal_moves(): #check against human list
            
            #figure out the first non free pos
            rowcount = self._first_free(m)
                        
            #then do the move
            tmp = self._state[rowcount - 1]
            self._state[rowcount - 1] = tmp[0:m] + self._next_move() + tmp[m + 1:] #stupid immutable strings
            self._moves = self._moves + 1  #increment move count
            return True
            
        else:
            print "not a legal move buddy"
            return False
        
        
    def undo_move(self, m):
        """undoes a move"""
        m = m - 1 #make start from 0
        
        #figure out the first non free pos
        rowcount = self._first_free(m)
                            
        #then undo the move if we are allowed to
        if rowcount < 6:
            tmp = self._state[rowcount]
            self._state[rowcount] = tmp[0:m] + '.' + tmp[m + 1:] #stupid immutable strings
            self._moves = self._moves - 1  #decrement the move count
            return True
        else:
            print "not a legal undo buddy"
            return False

    
    def _next_move(self):
        """figures out who would make the next move"""
        if self._moves % 2 == 0:
            return 'X'
        else:
            return 'O'
    
    def _count_turns(self):
        """figures out how many moves have been so far"""
        i = 0
        for column in self._state:
            i = i + column.count('X')
            i = i + column.count('O')
        return i
    
    
    def _first_free(self, m):
        """figure out the first non free position in col m counting down from top"""
        rowcount = 0

        for col in self._state:
            if col[m] == '.':
                rowcount = rowcount + 1
            else:
                break
        return rowcount
    
    
    def is_win(self):
        """returns tuple (bool, player) , true if current state is a win"""
        columns = map(lambda *row: list(row), *self._state) #transpose to look in columns

        def minicheck(s):
            if s.find("XXXX") != -1:
                return (True, "X")
            elif s.find("OOOO") != -1:
                return (True, "O")
            else:
                return (False, "better luck next time")
        
        #check horiz
        for row in self._state:
            (b,p) = minicheck(row)
            if b: return (b,p)
        
        #check vert
        for col in columns:
            s = "".join(col)
            (b,p) = minicheck(s)
            if b: return (b,p)
            
        #check diagonals
        #build coords
        xcoords = set([])
        ocoords = set([])
        for y, yv in enumerate(self._state):
            for x, xv in enumerate(yv):
                if xv == 'X':
                    xcoords.add((x,y))
                elif xv == 'O':
                    ocoords.add((x,y))
                    
        for x,y in xcoords:
            s = [frozenset([(x,y),(x-1,y-1),(x-2,y-2),(x-3,y-3)]),
                 frozenset([(x,y),(x+1,y+1),(x+2,y+2),(x+3,y+3)]),
                 frozenset([(x,y),(x+1,y-1),(x+2,y-2),(x+3,y-3)]),
                 frozenset([(x,y),(x-1,y+1),(x-2,y+2),(x-3,y+3)])]
            for d in s:
                if d.issubset(xcoords):
                    return (True, "X")
                
        for x,y in ocoords:
            s = [frozenset([(x,y),(x-1,y-1),(x-2,y-2),(x-3,y-3)]),
                 frozenset([(x,y),(x+1,y+1),(x+2,y+2),(x+3,y+3)]),
                 frozenset([(x,y),(x+1,y-1),(x+2,y-2),(x+3,y-3)]),
                 frozenset([(x,y),(x-1,y+1),(x-2,y+2),(x-3,y+3)])]
            for d in s:
                if d.issubset(ocoords):
                    return (True, "Y")
            
        return (False, ":-(")
        
        
                    
    def _test(self):
        """tests some stuff"""
        s = """
-1234567-
|.......|
|.......|
|.......|
|.......|
|..O....|
|.OXXXO.|
-1234567-"""
        print self.encode()
        self.decode(s)
        print self.legal_moves()
        self.do_move(3)
        print self.encode()
        self.undo_move(3)
        print self.encode()
        self.undo_move(3)
        print self.encode()
        self.undo_move(3)
        print self.encode()
        self.undo_move(3)
        print self.encode()
            
            
            
            
        
        